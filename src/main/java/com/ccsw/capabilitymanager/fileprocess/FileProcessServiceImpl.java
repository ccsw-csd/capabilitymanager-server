package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.activitydataimport.ActivityDataImportRepository;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.exception.FileProcessException;
import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.utils.UtilsService;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private VersionCertificacionesRepository versionCertificacionesRepository;
    
    @Autowired
	private CertificatesDataImportRepository certificatesDataImportRepository;
    
    @Autowired
	private ActivityDataImportRepository activityDataImportRepository;

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
            
            switch (file.getTipoFichero()) {
    		case "1":
    			//processStaffingDoc();
    			break;
    		case "2":
    			processRolsDoc(file, fileDownloaded);
    			break;
    		case "3":
				processCertificatesDoc(file, fileDownloaded);
    			break;
    		case "4":
    			//processItinerariosDoc();
    			break;
    		default:
    			//setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto,
    			//		Constants.ERROR_DOCUMENT_TYPE, Constants.ERROR_DOCUMENT_TYPE, Constants.EMPTY,
    			//		HttpStatus.BAD_REQUEST);
    		}

            
        });
    }

    private void processCertificatesDoc(FileProcess file, InputStream fileDownloaded) {
    	CapabilityLogger.logDebug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
		
		Sheet sheet = utilsService.obtainSheetFromInputStream(fileDownloaded);
        int numRegistros = sheet.getPhysicalNumberOfRows() - 1;
        
		VersionCertificaciones verCertificaciones = null;
		try {
			verCertificaciones = createCertificationesVersion(numRegistros, file.getTipoFichero(), file.getNombreFichero(), file.getNombreFichero(), file.getUsuario());
		} catch (Exception e) {
			CapabilityLogger.logError("Error creando la version del fichero de certificaciones . Error -> " + e.getMessage());
		}
		
		List<CertificatesDataImport> listCertificacionesDataImport = new ArrayList<>();
		List<ActivityDataImport> listActividadDataImport = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		ActivityDataImport certificateActivity = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			certificateActivity = new ActivityDataImport();
			String vcSAGA = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SAGA.getPosition());
			String vcPartner = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_PARTNER.getPosition());
			String vcCertificado = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICADO.getPosition());
			String vcNameGTD = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_NAME_GTD.getPosition());
			String vcCertificationGDT = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICATION_GTD.getPosition());
			String vcCode = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CODE.getPosition());
			String vcSector = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SECTOR.getPosition());
			String vcModulo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_MODULO.getPosition());
			String vcIdCandidato = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ID_CANDIDATO.getPosition());
			Date vcFechaCertificado = utilsService.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_CERTIFICADO.getPosition());
			Date vcFechaExpiracion = utilsService.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_EXPIRACION.getPosition());
			String vcActivo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ACTIVO.getPosition());
			String vcAnexo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ANEXO.getPosition());
			String vcComentarioAnexo = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_COMENTARIO_ANEXO.getPosition());
			String vcGgid = utilsService.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_GGID.getPosition());
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String vcFechaCertificadoStr = (vcFechaCertificado != null) ? dateFormat.format(vcFechaCertificado) : "";
			
			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(vcFechaCertificadoStr, "Fecha Certificado");
	        noNulables.put(vcSAGA, "SAGA");

		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	rollBackCertificates(verCertificaciones.getId());
                	throw new FileProcessException("La fila " + i + " no contiene el campo obligatorio: " + entry.getKey());
                }
            }
			
			data.setSAGA(vcSAGA);
			data.setPartner(vcPartner);
			data.setCertificado(vcCertificado);
			data.setNameGTD(vcNameGTD);
			data.setCertificationGTD(vcCertificationGDT);
			data.setCode(vcCode);
			data.setSecto(vcSector);
			data.setModulo(vcModulo);
			data.setIdCandidato(vcIdCandidato);
			data.setFechaCertificado(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			data.setFechaExpiracion(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			data.setActivo(vcActivo);
			data.setAnexo(vcAnexo);
			data.setComentarioAnexo(vcComentarioAnexo);
			data.setNumImportCodeId(verCertificaciones.getId());
			data.setGgid(vcGgid);

			
		
			certificateActivity.setsAGA(vcSAGA);
			certificateActivity.setPathwayId(vcCode);
			certificateActivity.setPathwayTitle(vcCertificado);
			certificateActivity.setCompletionPercent(100.00); 
			certificateActivity.setEnrollmentDate(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado); 
			certificateActivity.setCompletedDate(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			certificateActivity.setRecentActivityDate(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			certificateActivity.setObservaciones(vcComentarioAnexo);
			certificateActivity.setEstado("Finalizado");
			certificateActivity.setTypeActivity(7);
			
			if (!data.getSAGA().isEmpty()) {
				listCertificacionesDataImport.add(data);
				listActividadDataImport.add(certificateActivity);
			}
			currentRow = sheet.getRow(i);
		}

		if (listCertificacionesDataImport != null && !listCertificacionesDataImport.isEmpty()) {
			saveAllCertificatesDataImport(listCertificacionesDataImport);
			saveActividadDataImport(listActividadDataImport);
			
			file.setEstado(ESTADO_PROCESADO);
            fileProcessRepository.save(file);
            fileProcessRepository.flush();

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			CapabilityLogger.logError(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		CapabilityLogger.logDebug("[DataImportServiceImpl]       processCertificatesDoc >>>>");
		
	}

	private void processRolsDoc(FileProcess file, InputStream fileDownloaded) {
		
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
    
    /**
	 * Create an save on database CertificationsVersion Object (with
	 * CertificatesDataImport relations)
	 *
	 * @param numReg         num registers on Excel
	 * @param fileName       Excell File name
	 * @param description    Description
	 * @param user           User who uploads data
	 * @param idTipointerfaz idTipointerfaz value
	 * @param bs             File in byte array
	 * @return CapacityVersion Object inserted on database
	 * @throws IOException
	 */
	private VersionCertificaciones createCertificationesVersion(int numReg, String idTipointerfaz, String fileName, String description, String user) throws IOException {
		

		VersionCertificaciones versionCer = new VersionCertificaciones();
		if (idTipointerfaz.equals("3")) {
			idTipointerfaz = "Certifications";
		}
		versionCer.setIdTipointerfaz(idTipointerfaz);
		versionCer.setFechaImportacion(LocalDateTime.now());
		versionCer.setNumRegistros(numReg);
		versionCer.setNombreFichero(fileName);
		versionCer.setDescription(description);
		versionCer.setUsuario(user);
		versionCer.setFichero(dataserviceS3.getS3Endpoint());
		// versionCer.setFichero(dto.getFileData().getBytes());
		//		versionCer.setCertificates(setCertificacionesDataImport(versionCer, sheet));

		return versionCertificacionesRepository.save(versionCer);
	}
	
	private void rollBackCertificates(int id) {

		versionCertificacionesRepository.deleteById((long) id);
	}
	
	/**
	 * Save list CertificatesDataImport on database
	 *
	 * @param certificatesDataImportList List of objects to save
	 * @return List<CertificatesDataImport>
	 */
	@Transactional
	private void saveAllCertificatesDataImport(List<CertificatesDataImport> certificatesDataImportList) {
		try {
			//return certificatesDataImportRepository.saveAll(certificatesDataImportList);
			IntStream.range(0, (certificatesDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
            .mapToObj(i -> certificatesDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, certificatesDataImportList.size())))
            .forEach(subList -> {
                CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                certificatesDataImportRepository.saveAll(subList);
                certificatesDataImportRepository.flush();
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
	
	/**
	 * Save list ActivityImport on database
	 *
	 * @param ActividadDataImportList List of objects to save
	 * @return List<ActividadDataImport>
	 */
	@Transactional
	private void saveActividadDataImport(List<ActivityDataImport> actividadDataImportList) {
		try {
			//return activityDataImportRepository.saveAll(ActividadDataImportList);
			IntStream.range(0, (actividadDataImportList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
            .mapToObj(i -> actividadDataImportList.subList(i * BATCH_SIZE, Math.min((i+1) * BATCH_SIZE, actividadDataImportList.size())))
            .forEach(subList -> {
                CapabilityLogger.logInfo("Guardando " + subList.size() + " registros del fichero de roles.");
                activityDataImportRepository.saveAll(subList);
                activityDataImportRepository.flush();
            });
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEx = e.getMessage();
            String  respuesta = respuestaEx.substring(respuestaEx.indexOf('[')+1, respuestaEx.indexOf(']'));
            String respuestaEr = respuesta + ". Error procesando el excel. Comprueba los datos correctos";
			CapabilityLogger.logError(respuestaEr);
			throw new UnprocessableEntityException(respuestaEr);
		}
	}
}
