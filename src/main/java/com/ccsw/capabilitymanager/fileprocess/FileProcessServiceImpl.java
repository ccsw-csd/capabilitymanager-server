package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.exception.FileProcessException;
import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.utils.UtilsService;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
public class FileProcessServiceImpl implements FileProcessService{

    private static final String ESTADO_PROCESANDO = "PROCESANDO";
    private static final String ESTADO_PROCESADO = "PROCESADO";

    private static final int BATCH_SIZE = 700;

    @Autowired
    private FileProcessRepository fileProcessRepository;

    @Autowired
    private VersionCapacidadesRepository versionCapacidadesRepository;

    @Autowired
    private FormDataImportRepository formDataImportRepository;

    @Autowired
    private DataserviceS3 dataserviceS3;

    @Autowired
    private UtilsService utilsService;

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> processPendingFile(FileProcess file) throws Exception {
        return CompletableFuture.runAsync(() -> {
            MinioClient minioClient = dataserviceS3.getMinioClient();
            InputStream fileDownloaded = null;

            try {
                fileDownloaded = minioClient.getObject(GetObjectArgs.builder().bucket(file.getNombreBucket()).object(file.getNombreFichero()).build());
            } catch (Exception e) {
                CapabilityLogger.logError("Error obteniendo el fichero " + file.getNombreFichero() + " de S3.");
                throw new FileProcessException("Error obteniendo el fichero " + file.getNombreFichero() + " de S3.");
            }

            file.setEstado(ESTADO_PROCESANDO);
            fileProcessRepository.save(file);
            fileProcessRepository.flush();

            //Aqui la gestion propia del fichero
            //Con el fichero descargado de S3, obtenemos la hoja de Excel
            Sheet sheet = utilsService.obtainSheetFromInputStream(fileDownloaded);
            int numRegistros = sheet.getPhysicalNumberOfRows() - 1;

            //Se crea la version en la tabla de capacidades
            //TODO: Gestion de versiones de los ficheros. Si tenemos ya X versiones guardadas, habrá que eliminar la primera disponible que no se este utilizando en ningun
            // informe
            VersionCapacidades version = null;
            try {
                version = createCapacityVersion(numRegistros, file.getNombreFichero(), file.getNombreFichero(), file.getUsuario(), file.getTipoFichero());
            } catch (Exception e) {
                CapabilityLogger.logError("Error creando la version del fichero de capacidades. Error -> " + e.getMessage());
                throw e;
            }

            List<FormDataImport> roleDataList = null;

            try {
                roleDataList = generateRoleDataList(sheet, version);
            } catch (Exception e) {
                CapabilityLogger.logError("Error obteniendo los datos del fichero. Error -> " + e.getMessage());
                throw e;
            }

            if (roleDataList != null && !roleDataList.isEmpty()) {
                saveAllFormDataImport(roleDataList);

                file.setEstado(ESTADO_PROCESADO);
                fileProcessRepository.save(file);
                fileProcessRepository.flush();
            } else {
                StringBuilder errorData = new StringBuilder();
                errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
                        .append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_ROL_FILE);

                CapabilityLogger.logError(errorData.toString());

                throw new UnprocessableEntityException(Constants.ERROR_EMPTY_ROL_FILE);
            }
        });
    }

    /**
     * Create an save on database CapacityVersion Object
     *
     * @param numReg         num registers on Excel
     * @param fileName       Excell File name
     * @param description    Description
     * @param user           User who uploads data
     * @param idTipointerfaz idTipointerfaz value
     * @param bs             File in byte array
     * @return CapacityVersion Object inserted on database
     */
    private VersionCapacidades createCapacityVersion(int numReg, String fileName, String description, String user,
            String idTipointerfaz) {
        VersionCapacidades versionCap = new VersionCapacidades();

        versionCap.setNumRegistros(numReg);
        versionCap.setFechaImportacion(LocalDateTime.now());
        versionCap.setNombreFichero(fileName);
        versionCap.setDescripcion(description);
        versionCap.setUsuario(user);
        if (idTipointerfaz.equals("2")) {
            idTipointerfaz = "Roles";
        }
        versionCap.setIdTipointerfaz(idTipointerfaz);
        versionCap.setFichero(dataserviceS3.getS3Endpoint());

        return versionCapacidadesRepository.save(versionCap);
    }

    private List<FormDataImport> generateRoleDataList(Sheet sheet, VersionCapacidades version) throws FileProcessException {
        List<FormDataImport> rolDataList = new ArrayList<>();
        Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);

        for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
            FormDataImport data = new FormDataImport();

            String vcProfileSAGA = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SAGA.getPosition());
            String vcProfileEmail = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_EMAIL.getPosition());
            String vcProfileName = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_NAME.getPosition());
            String vcProfileRoll1Extendido = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition());
            String vcProfileRoll2EM = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_EM.getPosition());
            String vcProfileRoll2AR = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AR.getPosition());
            String vcProfileRoll2AN = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AN.getPosition());
            String vcProfileRoll2SE = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_SE.getPosition());
            String vcProfileRolExperienceEM = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_EM.getPosition());
            String vcProfileRolExperienceAR = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_AR.getPosition());
            String vcProfileRoll3 = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL3.getPosition());
            String vcProfileSkillCloudNativeExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_NATIVE_EXPERIENCE.getPosition());
            String vcProfileSkillLowCodeExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_LOWCODE_EXPERIENCE.getPosition());
            String vcProfileRoll4 = utilsService.getStringValue(currentRow,	Constants.RolsDatabasePos.COL_ROLL4.getPosition());
            String vcProfileSectorExperience = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SECTOR_EXPERIENCE.getPosition());
            String vcProfileSkillCloudExp = utilsService.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_EXPERIENCE.getPosition());

            Map<String, String> noNulables = new HashMap<>();
            noNulables.put("Saga", vcProfileSAGA);

            Map.Entry<String, String> nullField = noNulables.entrySet().stream()
                    .filter(entry -> entry.getValue() == null || entry.getValue().isEmpty())
                    .findFirst().orElse(null);
            if(nullField != null)
                throw new FileProcessException("La fila " + i + " no contiene el campo obligatorio: " + nullField.getKey());

            data.setSAGA(vcProfileSAGA);
            data.setEmail(vcProfileEmail);
            data.setName(vcProfileName);
            data.setRolL1Extendido(vcProfileRoll1Extendido);
            data.setRolL2EM(vcProfileRoll2EM);
            data.setRolL2AR(vcProfileRoll2AR);
            data.setRolL2AN(vcProfileRoll2AN);
            data.setRolL2SE(vcProfileRoll2SE);
            data.setRolExperienceEM(vcProfileRolExperienceEM);
            data.setRolExperienceAR(vcProfileRolExperienceAR);
            data.setRolL3(vcProfileRoll3);
            data.setRolL4(vcProfileRoll4);
            data.setSkillCloudNativeExperience(vcProfileSkillCloudNativeExperience);
            data.setSkillLowCodeExperience(vcProfileSkillLowCodeExperience);
            data.setSectorExperience(vcProfileSectorExperience);
            data.setSkillCloudExp(vcProfileSkillCloudExp);
            data.setNumImportCodeId(version.getId());
            setRolL1(data);

            rolDataList.add(data);
            currentRow = sheet.getRow(i);
        }

        return rolDataList;
    }

    /**
     * Sets the RolL1 profile value in the {@link FormDataImport} object based on the extended role description.
     *
     * <p>This method evaluates the extended role description from the {@link FormDataImport} object and sets
     * the appropriate RolL1 profile value based on the role description. If the description starts with a specific
     * role title, it sets the corresponding RolL1 profile constant. If the role title does not match any predefined
     * roles, it sets an empty value.</p>
     *
     * @param formDataImport The {@link FormDataImport} object containing the role description and where the RolL1
     *                       profile value will be set.
     */
    private void setRolL1(FormDataImport formDataImport) {
        String rolL1extendido = formDataImport.getRolL1extendido();

        if (rolL1extendido.startsWith("Software Engineer")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_SE);
        } else if (rolL1extendido.startsWith("Business Analyst")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_BA);
        } else if (rolL1extendido.startsWith("Engagement Manager")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_EM);
        } else if (rolL1extendido.startsWith("Architect")) {
            formDataImport.setVcProfileRolL1(Constants.ROLL1_AR);
        } else {
            formDataImport.setVcProfileRolL1(Constants.EMPTY);
        }
    }

    private void saveAllFormDataImport(List<FormDataImport> formDataImportList) {
        try {
            //return formDataImportRepository.saveAll(formDataImportList);
            IntStream.range(0, (formDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                    .mapToObj(i -> formDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, formDataImportList.size())))
                    .forEach(subList -> {
                        CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                        formDataImportRepository.saveAll(subList);
                        formDataImportRepository.flush();
                    });
        } catch (Exception e) {
            StringBuilder errorData = new StringBuilder();
            errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
                    .append(Constants.ERROR_INIT2);
            CapabilityLogger.logError(errorData.toString() + e.getMessage());
            String respuestaEx = e.getMessage();
            String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
            String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
            throw new UnprocessableEntityException(respuestaEr);
        }
    }
}
