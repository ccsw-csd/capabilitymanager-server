package com.ccsw.capabilitymanager.dataimport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.ccsw.capabilitymanager.websocket.WebSocketService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.fileprocess.S3Service;
import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.activitydataimport.ActivityDataImportRepository;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.itinerariosdataimport.ItinerariosDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.model.ItinerariosDataImport;
import com.ccsw.capabilitymanager.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.capabilitymanager.staffingdataimport.model.StaffingDataImport;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.versionitinerarios.VersionItinerariosRepository;
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DataImportServiceImpl implements DataImportService {

	@Autowired
	private FormDataImportRepository formDataImportRepository;

	@Autowired
	private StaffingDataImportRepository staffingDataImportRepository;

	@Autowired
	private CertificatesDataImportRepository certificatesDataImportRepository;
		
	@Autowired
	private ItinerariosDataImportRepository itinerariosDataImportRepository;
	
	@Autowired
	private VersionCapacidadesRepository versionCapacidadesRepository;

	@Autowired
	private VersionStaffingRepository versionStaffingRepository;
	
	@Autowired
	private VersionItinerariosRepository versionItinerariosRepository;

	@Autowired
	private VersionCertificacionesRepository versionCertificacionesRepository;

	@Autowired
	private UtilsServiceImpl utilsServiceImpl;

	@Autowired
	private DataserviceS3 dataservice;

	@Autowired
	private S3Service s3service;

	@Autowired
	private ActivityDataImportRepository activityDataImportRepository;

	@Autowired
	private WebSocketService webSocketService;

	private static final int BATCH_SIZE = 700;

	/**
	 * Processes the given import request based on the document type specified.
	 *
	 * <p>This method handles different types of documents specified by the `documentType` field in the
	 * {@link ImportRequestDto}. It uploads the file to S3, checks the input object, and processes it based
	 * on the document type. It returns an {@link ImportResponseDto} with the result of the processing.</p>
	 *
	 * @param dto The {@link ImportRequestDto} containing the file and document type to process.
	 * @return An {@link ImportResponseDto} with the result of the processing, including any errors or success messages.
	 */
	@Override
	@Async
	public ImportResponseDto processObject(ImportRequestDto dto) {
		CapabilityLogger.logDebug("[DataImportServiceImpl]  >>>> processObject ");
		dataservice.getMinioClient();
		s3service.uploadFile(dto);

		utilsServiceImpl.checkInputObject(dto);
		var importResponseDto = new ImportResponseDto();

		switch (dto.getDocumentType()) {
		case "1":
			importResponseDto = processStaffingDoc(dto);
			break;
		case "2":
			importResponseDto = processRolsDoc(dto);
			break;
		case "3":
			importResponseDto = processCertificatesDoc(dto);
			break;
		case "4":
			importResponseDto = processItinerariosDoc(dto);
			break;
		default:
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto,
					Constants.ERROR_DOCUMENT_TYPE, Constants.ERROR_DOCUMENT_TYPE, Constants.EMPTY,
					HttpStatus.BAD_REQUEST);
		}

		webSocketService.notifyClient("Procesado completo.");

		CapabilityLogger.logDebug("[DataImportServiceImpl] processObject >>>>");
		return importResponseDto;

	}

	/**
	 * Process Rol Document received
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	@Transactional
	private ImportResponseDto processRolsDoc(ImportRequestDto dto) {
		CapabilityLogger.logDebug(" >>>> processRolsDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();
		importResponseDto.setBucketName(dataservice.getBucketName());
		importResponseDto.setPath(dataservice.getS3Endpoint());

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int sizeSheet = sheet.getPhysicalNumberOfRows() - 1;

		VersionCapacidades verCap = null;
		try {
			verCap = createCapacityVersion(sizeSheet, dto.getFileData().getOriginalFilename(), dto.getDescription(),
					dto.getUser(), dto.getDocumentType());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e,
					HttpStatus.UNPROCESSABLE_ENTITY);
			return importResponseDto;
		}

		Map<String, Object> roleData = generateRoleDataList(sheet, verCap);
		ImportResponseDto error = (ImportResponseDto) roleData.get("error");

		if(error != null) {
			rollBackRoles(verCap.getId());

			return error;
		}

		List<FormDataImport> formDataImportList = (List<FormDataImport>) roleData.get("data");

		if (formDataImportList != null && !formDataImportList.isEmpty()) {
			saveAllFormDataImport(formDataImportList);
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT)
					.append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_ROL_FILE);

			CapabilityLogger.logError(errorData.toString());

			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_ROL_FILE);
		}

		CapabilityLogger.logDebug("      processRolsDoc >>>>");
		return importResponseDto;

	}

	private Map<String, Object> generateRoleDataList(Sheet sheet, VersionCapacidades version) {
		Map<String, Object> result = new HashMap<>();
		List<FormDataImport> rolDataList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);

		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			FormDataImport data = new FormDataImport();

			String vcProfileSAGA = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SAGA.getPosition());
			String vcProfileEmail = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_EMAIL.getPosition());
			String vcProfileName = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_NAME.getPosition());
			String vcProfileRoll1Extendido = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition());
			String vcProfileRoll2EM = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_EM.getPosition());
			String vcProfileRoll2AR = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AR.getPosition());
			String vcProfileRoll2AN = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_AN.getPosition());
			String vcProfileRoll2SE = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL2_SE.getPosition());
			String vcProfileRolExperienceEM = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_EM.getPosition());
			String vcProfileRolExperienceAR = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_AR.getPosition());
			String vcProfileRoll3 = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_ROLL3.getPosition());
			String vcProfileSkillCloudNativeExperience = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_NATIVE_EXPERIENCE.getPosition());
			String vcProfileSkillLowCodeExperience = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_LOWCODE_EXPERIENCE.getPosition());
			String vcProfileRoll4 = utilsServiceImpl.getStringValue(currentRow,	Constants.RolsDatabasePos.COL_ROLL4.getPosition());
			String vcProfileSectorExperience = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SECTOR_EXPERIENCE.getPosition());
			String vcProfileSkillCloudExp = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_SKILL_CLOUD_EXPERIENCE.getPosition());

			Map<String, String> noNulables = new HashMap<>();
			noNulables.put("Saga", vcProfileSAGA);

			ImportResponseDto error = checkNullFields(noNulables, i);
			if(error != null) {
				result.put("error", error);

				return result;
			}

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

		result.put("data", rolDataList);

		return result;
	}

	/**
	 * Este metodo comprobara si alguno los campos que son obligatorios es nulo
	 *
	 * @param fields
	 * @return
	 */
	private ImportResponseDto checkNullFields(Map<String, String> fields, int rowNum) {
		Map.Entry<String, String> nullField = fields.entrySet().stream().filter(entry -> entry.getValue() == null || entry.getValue().isEmpty()).findFirst().orElse(null);

		ImportResponseDto error = null;
		if(nullField != null) {
			error = new ImportResponseDto();

			error.setStatus(HttpStatus.BAD_REQUEST);
			error.setMessage("Row " + rowNum + " is missing required field: " + nullField.getKey());
			error.setError("Row " + rowNum + " is missing required field: " + nullField.getKey());
		}

		return error;
	}

	/**
	 * Process Staffing Document received
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	@Transactional
	private ImportResponseDto processStaffingDoc(ImportRequestDto dto) {
		CapabilityLogger.logDebug("[DataImportServiceImpl]  >>>> processStaffingDoc ");

		ImportResponseDto importResponseDto = new ImportResponseDto();
		importResponseDto.setBucketName(dataservice.getBucketName());
		importResponseDto.setPath(dataservice.getS3Endpoint());

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int sizeSheet = sheet.getPhysicalNumberOfRows() - 1;
		VersionStaffing verStaf = null;
		try {
			verStaf = createStaffingVersion(sizeSheet, dto.getFileData().getOriginalFilename(), dto.getDescription(),
					dto.getUser(), dto.getDocumentType());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e,
					HttpStatus.UNPROCESSABLE_ENTITY);
			return importResponseDto;
		}

		List<StaffingDataImport> staffingDataImportList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		StaffingDataImport data = new StaffingDataImport();
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new StaffingDataImport();
			String saga = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_SAGA.getPosition());
			String ggid = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_GGID.getPosition());
			String centro = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_CENTRO.getPosition());
			String nombre = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_NOMBRE.getPosition());
			String apellidos = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_APELLIDOS.getPosition());
			String localizacion = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_LOCALIZACION.getPosition());
			String practica = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PRACTICA.getPosition());
			String grado = utilsServiceImpl.getGradeValue(currentRow, Constants.StaffingDatabasePos.COL_GRADO.getPosition());
			String categoria = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_CATEGORIA.getPosition());
			String perfilTecnico = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PERFIL_TECNICO.getPosition());
			String primarySkill = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PRIMARY_SKILL.getPosition());
			Date fechaIncorporacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_FECHA_INCORPORACION.getPosition());
			Integer porcentajeAsignacion = utilsServiceImpl.getIntValue(currentRow, Constants.StaffingDatabasePos.COL_PORCENTAJE_ASIGNACION.getPosition());
			String status = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_STATUS.getPosition());
			String clienteActual = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_CLIENTE_ACTUAL.getPosition());
			Date fechaInicioAsignacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_FECHA_INICIO_ASIGNACION.getPosition());
			Date fechaFinAsignacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_FECHA_FIN_ASIGNACION.getPosition());
			Date fechaDisponibilidad = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_FECHA_DISPONIBILIDAD.getPosition());
			String posicionProyectoFuturo = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_POSICION_PROYECTO_FUTURO.getPosition());
			String colaboraciones = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_COLABORACIONES.getPosition());
			String proyectoAnterior = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PROYECTO_ANTERIOR.getPosition());
			String inglesEscrito = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_INGLES_ESCRITO.getPosition());
			String inglesHablado = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_INGLES_HABLADO.getPosition());
			String jornada = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_JORNADA.getPosition());
			String vcProfileMesesBench = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_MESES_BENCH.getPosition());
			String practiceArea = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PRACTICE_AREA.getPosition());

			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(saga, "Saga");
	        noNulables.put(ggid, "Ggid");
	        noNulables.put(centro, "Centro");
	        noNulables.put(nombre, "Nombre");

		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	ImportResponseDto error = new ImportResponseDto();
                	error.setStatus(HttpStatus.BAD_REQUEST);
                	error.setMessage("Row " + i + " is missing required field: " + entry.getValue());
                	error.setError("Row " + i + " is missing required field: " + entry.getValue());
                	rollBackStaffing(verStaf.getId());
                    return error;
                }
            }
		
			data.setNumImportCode(verStaf.getId());
			data.setSaga(saga);
			data.setGgid(ggid);
			data.setCentro(centro);
			data.setNombre(nombre);
			data.setApellidos(apellidos);
			data.setLocalizacion(localizacion);
			data.setPractica(practica);
			data.setGrado(grado);
			data.setCategoria(categoria);
			data.setPerfilTecnico(perfilTecnico);
			data.setPrimarySkill(primarySkill);
			data.setFechaIncorporacion(fechaIncorporacion);
			data.setAsignacion(porcentajeAsignacion);
			data.setStatus(status);
			data.setClienteActual(clienteActual);
			data.setFechaInicioAsignacion(fechaInicioAsignacion);
			data.setFechaFinAsignacion(fechaFinAsignacion);
			data.setFechaDisponibilidad(fechaDisponibilidad);
			data.setPosicionProyectoFuturo(posicionProyectoFuturo);
			data.setColaboraciones(colaboraciones);
			data.setProyectoAnterior(proyectoAnterior);
			data.setInglesEscrito(inglesEscrito);
			data.setInglesHablado(inglesHablado);
			data.setJornada(jornada);
			data.setMesesBench(vcProfileMesesBench);
			data.setPracticeArea(practiceArea);

			staffingDataImportList.add(data);
			currentRow = sheet.getRow(i);
		}

		if (staffingDataImportList != null && !staffingDataImportList.isEmpty()) {
			saveAllStaffingDataImport(staffingDataImportList);

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			CapabilityLogger.logError(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		CapabilityLogger.logDebug(" [DataImportServiceImpl]      processStaffingDoc >>>>");
		return importResponseDto;

	}

	/**
	 * Process Certification Document received (waitting specificatios)
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	private ImportResponseDto processCertificatesDoc(ImportRequestDto dto) {
		CapabilityLogger.logDebug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();
		importResponseDto.setBucketName(dataservice.getBucketName());
		importResponseDto.setPath(dataservice.getS3Endpoint());

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		VersionCertificaciones verCertificaciones = null;
		try {
			verCertificaciones = createCertificationesVersion(dto);
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e,
					HttpStatus.UNPROCESSABLE_ENTITY);
			return importResponseDto;
		}

		List<CertificatesDataImport> listCertificacionesDataImport = new ArrayList<>();
		List<ActivityDataImport> listActividadDataImport = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		ActivityDataImport certificateActivity = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			certificateActivity = new ActivityDataImport();
			String vcSAGA = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SAGA.getPosition());
			String vcPartner = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_PARTNER.getPosition());
			String vcCertificado = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICADO.getPosition());
			String vcNameGTD = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_NAME_GTD.getPosition());
			String vcCertificationGDT = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CERTIFICATION_GTD.getPosition());
			String vcCode = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_CODE.getPosition());
			String vcSector = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_SECTOR.getPosition());
			String vcModulo = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_MODULO.getPosition());
			String vcIdCandidato = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ID_CANDIDATO.getPosition());
			Date vcFechaCertificado = utilsServiceImpl.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_CERTIFICADO.getPosition());
			Date vcFechaExpiracion = utilsServiceImpl.getDateValue(currentRow,
					Constants.CertificatesDatabasePos.COL_FECHA_EXPIRACION.getPosition());
			String vcActivo = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ACTIVO.getPosition());
			String vcAnexo = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_ANEXO.getPosition());
			String vcComentarioAnexo = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_COMENTARIO_ANEXO.getPosition());
			String vcGgid = utilsServiceImpl.getStringValue(currentRow,
					Constants.CertificatesDatabasePos.COL_GGID.getPosition());
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String vcFechaCertificadoStr = (vcFechaCertificado != null) ? dateFormat.format(vcFechaCertificado) : "";
			
			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(vcFechaCertificadoStr, "Fecha Certificado");
	        noNulables.put(vcSAGA, "SAGA");



		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	ImportResponseDto error = new ImportResponseDto();
                	error.setStatus(HttpStatus.BAD_REQUEST);
                	error.setMessage("Row " + i + " is missing required field: " + entry.getValue());
                	error.setError("Row " + i + " is missing required field: " + entry.getValue());
                	rollBackCertificates(verCertificaciones.getId());
                    return error;
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

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			CapabilityLogger.logError(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		CapabilityLogger.logDebug("[DataImportServiceImpl]       processCertificatesDoc >>>>");
		return importResponseDto;
	}
	
	/**
	 * Process Itinerarios Document received (waitting specificatios)
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	private ImportResponseDto processItinerariosDoc(ImportRequestDto dto) {
		CapabilityLogger.logDebug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();
		importResponseDto.setBucketName(dataservice.getBucketName());
		importResponseDto.setPath(dataservice.getS3Endpoint());

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		VersionItinerarios verItinerarios = null;
		try {
			verItinerarios = createItinerariosVersion(dto);
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e,
					HttpStatus.UNPROCESSABLE_ENTITY);
			return importResponseDto;
		}

		List<ItinerariosDataImport> listItinerariosDataImport = new ArrayList<>();
		List<ActivityDataImport> listActividadDataImport = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		ItinerariosDataImport data = null;
		ActivityDataImport dataActivity = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new ItinerariosDataImport();
			dataActivity = new ActivityDataImport();
		
			String vcGGID = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_GGID.getPosition());
			String vcFirstName= utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_FIRST_NAME.getPosition());
			String vcLastName = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_LAST_NAME.getPosition());
			String vcEmailId = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_EMAIL_ID.getPosition());
			String vcGlobalGrade = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_GLOBAL_GRADE.getPosition());
			String vcCountry = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_COUNTRY.getPosition());
			String vcSbu = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_SBU.getPosition());
			String vcBu = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_BU.getPosition());
			String vcPathwayId = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_PATHWAY_ID.getPosition());
			String vcPathwayTitle = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_PATHWAY_TITLE.getPosition());
			Integer vcTotalPath = utilsServiceImpl.getIntValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_TOTAL_PATH.getPosition());
			Integer vcCompletedContent = utilsServiceImpl.getIntValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_COMPLETED_CONTENT.getPosition());
			String vcCompletedPercent = utilsServiceImpl.getStringValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_COMPLETION_PERCENT.getPosition());
			Date vcEnrollmentDate = utilsServiceImpl.getDateValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_ENROLLMENT_DATE.getPosition());
			Date vcRecentActivity= utilsServiceImpl.getDateValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_RECENT_ACTIVITY.getPosition());
			Date vcCompletedDate = utilsServiceImpl.getDateValue(currentRow,
					Constants.ItinerariosDatabasePos.COL_COMPLETED_DATE.getPosition());

			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(vcGGID, "GGID");
	        noNulables.put(vcPathwayId, "PathwayId");
	        noNulables.put(vcFirstName, "FirstName");
	        noNulables.put(vcLastName, "LastName");



		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	ImportResponseDto error = new ImportResponseDto();
                	error.setStatus(HttpStatus.BAD_REQUEST);
                	error.setMessage("Row " + i + " is missing required field: " + entry.getValue());
                	error.setError("Row " + i + " is missing required field: " + entry.getValue());
                	rollBackItinerarios(verItinerarios.getId());
                    return error;
                }
            }
      
			data.setGGID(vcGGID);
			data.setFirstName(vcFirstName);
			data.setLastName(vcLastName);
			data.setEmail(vcEmailId);
			data.setGlobalGrade(vcGlobalGrade);
			data.setCountry(vcCountry);
			data.setSbu(vcSbu);
			data.setBu(vcBu);
			data.setPathwayId(vcPathwayId);
			data.setPathwayTitle(vcPathwayTitle);
			data.setTotalPathwayContent(vcTotalPath);
			data.setCompletedContent(vcCompletedContent);
			data.setCompletionPercent(vcCompletedPercent); 
			data.setEnrollmentDate(vcEnrollmentDate); 
			data.setRecentActivityDate(vcRecentActivity);
			data.setCompletedDate(vcCompletedDate);
			
			String numericPercentage = vcCompletedPercent.replace("%", "");
			Double numericValue = Double.parseDouble(numericPercentage);
			
			dataActivity.setgGID(vcGGID);
			dataActivity.setPathwayId(vcPathwayId);
			dataActivity.setPathwayTitle(vcPathwayTitle);
			dataActivity.setCompletionPercent(numericValue); 
			dataActivity.setEnrollmentDate(vcEnrollmentDate);
			if (numericValue == 0.00 ) {
			dataActivity.setEstado("No iniciado");
			}else {
			dataActivity.setEstado("Iniciado");	
			}
			dataActivity.setRecentActivityDate(vcRecentActivity);		
			dataActivity.setTypeActivity(8);
			
			
			
			if (!data.getGGID().isEmpty()) {
				listItinerariosDataImport.add(data);
				listActividadDataImport.add(dataActivity);
			}
			
			currentRow = sheet.getRow(i);
		}

		if (listItinerariosDataImport != null && !listItinerariosDataImport.isEmpty()) {
			saveAllItinerariosDataImport(listItinerariosDataImport);
			saveActividadDataImport(listActividadDataImport);

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			CapabilityLogger.logError(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		CapabilityLogger.logDebug("[DataImportServiceImpl]       processItinerariosDoc >>>>");
		return importResponseDto;
	}

	/**
	 * Rolls back or reverts the staffing record with the given ID.
	 *
	 * <p>This method deletes the staffing record from the repository using the provided ID. The ID is
	 * cast to a {@code Long} before performing the delete operation.</p>
	 *
	 * @param id The ID of the staffing record to be deleted. This is an integer that is cast to a {@code Long}.
	 */
	private void rollBackStaffing(int id) {

		versionStaffingRepository.deleteById((long) id);
	}
	
	/**
	 * Rolls back or reverts the certificate record with the given ID.
	 *
	 * <p>This method deletes the certificate record from the repository using the provided ID. The ID is
	 * cast to a {@code Long} before performing the delete operation.</p>
	 *
	 * @param id The ID of the certificate record to be deleted. This is an integer that is cast to a {@code Long}.
	 */
	private void rollBackCertificates(int id) {

		versionCertificacionesRepository.deleteById((long) id);
	}
	
	/**
	 * Rolls back or reverts the roles record with the given ID.
	 *
	 * <p>This method deletes the role record from the repository using the provided ID. The ID is
	 * cast to a {@code Long} before performing the delete operation.</p>
	 *
	 * @param id The ID of the role record to be deleted. This is an integer that is cast to a {@code Long}.
	 */
	private void rollBackRoles(int id) {

		versionCapacidadesRepository.deleteById((long) id);
	}
	
	/**
	 * Rolls back or reverts the itinerario record with the given ID.
	 *
	 * <p>This method deletes the itinerario record from the repository using the provided ID. The ID is
	 * cast to a {@code Long} before performing the delete operation.</p>
	 *
	 * @param id The ID of the itinerario record to be deleted. This is an integer that is cast to a {@code Long}.
	 */
	private void rollBackItinerarios(int id) {

		versionItinerariosRepository.deleteById((long) id);
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
		versionCap.setFichero(dataservice.getS3Endpoint());

		return versionCapacidadesRepository.save(versionCap);
	}

	/**
	 * Create an save on database VersionStaffing Object
	 *
	 * @param numReg         num registers on Excel
	 * @param fileName       Excell File name
	 * @param description    Description
	 * @param user           User who uploads data
	 * @param idTipointerfaz idTipointerfaz value
	 * @param bs             File in byte array
	 * @return VersionStaffing Object inserted on database
	 */
	private VersionStaffing createStaffingVersion(int numReg, String fileName, String description, String user,
			String idTipointerfaz) {
		VersionStaffing versionStaf = new VersionStaffing();
		versionStaf.setNumRegistros(numReg);
		versionStaf.setFechaImportacion(LocalDateTime.now());
		versionStaf.setNombreFichero(fileName);
		versionStaf.setDescripcion(description);
		versionStaf.setUsuario(user);
		if (idTipointerfaz.equals("1")) {
			idTipointerfaz = "Staffing";
		}
		versionStaf.setIdTipointerfaz(idTipointerfaz);
		versionStaf.setFichero(dataservice.getS3Endpoint());

		return versionStaffingRepository.save(versionStaf);
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
	private VersionCertificaciones createCertificationesVersion(ImportRequestDto dto) throws IOException {
		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int numReg = sheet.getPhysicalNumberOfRows() - 1;

		VersionCertificaciones versionCer = new VersionCertificaciones();
		String idTipointerfaz = dto.getDocumentType();
		if (idTipointerfaz.equals("3")) {
			idTipointerfaz = "Certifications";
		}
		versionCer.setIdTipointerfaz(idTipointerfaz);
		versionCer.setFechaImportacion(LocalDateTime.now());
		versionCer.setNumRegistros(numReg);
		versionCer.setNombreFichero(dto.getFileData().getOriginalFilename());
		versionCer.setDescription(dto.getDescription());
		versionCer.setUsuario(dto.getUser());
		// versionCer.setFichero(dto.getFileData().getBytes());
		//		versionCer.setCertificates(setCertificacionesDataImport(versionCer, sheet));

		return versionCertificacionesRepository.save(versionCer);
	}
	
	/**
	 * Create an save on database ItinerariosVersion Object (with
	 * ItinerariosDataImport relations)
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
	private VersionItinerarios createItinerariosVersion(ImportRequestDto dto) throws IOException {
		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int numReg = sheet.getPhysicalNumberOfRows() - 1;

		VersionItinerarios versionIti = new VersionItinerarios();
		versionIti.setIdTipointerfaz(Integer.valueOf(dto.getDocumentType()));
		versionIti.setFechaImportacion(LocalDateTime.now());
		versionIti.setNumRegistros(numReg);
		versionIti.setNombreFichero(dto.getFileData().getOriginalFilename());
		versionIti.setDescripcion(dto.getDescription());
		versionIti.setUsuario(dto.getUser());
		// versionCer.setFichero(dto.getFileData().getBytes());
		//		versionCer.setCertificates(setCertificacionesDataImport(versionCer, sheet));
		return versionItinerariosRepository.save(versionIti);
	}
	

	/**
	 * Save list FormDataImport on database
	 *
	 * @param formDataImportList List Object FormDataImport
	 * @return List<FormDataImport>
	 */
	@Transactional
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
	 * Save list StaffingDataImport on database
	 *
	 * @param staffingDataImportList List Object StaffingDataImport
	 * @return List<StaffingDataImport>
	 */
	@Transactional
	private List<StaffingDataImport> saveAllStaffingDataImport(List<StaffingDataImport> staffingDataImportList) {
		try {
			return staffingDataImportRepository.saveAll(staffingDataImportList);
		}

		catch (Exception e) {
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
	 * Save list CertificatesDataImport on database
	 *
	 * @param certificatesDataImportList List of objects to save
	 * @return List<CertificatesDataImport>
	 */
	@Transactional
	private List<CertificatesDataImport> saveAllCertificatesDataImport(
			List<CertificatesDataImport> certificatesDataImportList) {
		try {
			return certificatesDataImportRepository.saveAll(certificatesDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}
	
	/**
	 * Save list ItinerariosDataImport on database
	 *
	 * @param ItinerariosDataImportList List of objects to save
	 * @return List<ItinerariosDataImport>
	 */
	@Transactional
	private List<ItinerariosDataImport> saveAllItinerariosDataImport(
			List<ItinerariosDataImport> itinerariosDataImportList) {
		try {
			return itinerariosDataImportRepository.saveAll(itinerariosDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}
	
	/**
	 * Save list ItinerariosDataImport on database
	 *
	 * @param ItinerariosDataImportList List of objects to save
	 * @return List<ItinerariosDataImport>
	 */
	/*
	@Transactional
	private List<ItinerariosActividadDataImport> saveItinerariosActividadDataImport(
			List<ItinerariosActividadDataImport> itinerariosActividadDataImportList) {
		try {
			return itinerariosActividadDataImportRepository.saveAll(itinerariosActividadDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}
	*/
	
	/**
	 * Save list CertificadosImport on database
	 *
	 * @param CertificadosDataImportList List of objects to save
	 * @return List<ItinerariosDataImport>
	 */
	/*
	@Transactional
	private List<CertificatesActividadDataImport> saveCertificadosActividadDataImport(
			List<CertificatesActividadDataImport> certificadosActividadDataImportList) {
		try {
			return certificatesActividadDataImportRepository.saveAll(certificadosActividadDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}*/

	/**
	 * Save list ActivityImport on database
	 *
	 * @param ActividadDataImportList List of objects to save
	 * @return List<ActividadDataImport>
	 */
	@Transactional
	private List<ActivityDataImport> saveActividadDataImport(
			List<ActivityDataImport> ActividadDataImportList) {
		try {
			return activityDataImportRepository.saveAll(ActividadDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2);
			CapabilityLogger.logError(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			CapabilityLogger.logError(respuestaEr);
			throw new UnprocessableEntityException(respuestaEr);
		}
	}

	/**
	 * Sets an error message on the {@link ImportResponseDto} and logs the error details.
	 *
	 * <p>This method constructs an error message including the function name and details from the given exception.
	 * It then calls another overload of {@code setErrorToReturn} to set the error message on the 
	 * {@link ImportResponseDto} and log the error details.</p>
	 *
	 * @param function The name of the function where the error occurred. Used for constructing the error message.
	 * @param importResponseDto The {@link ImportResponseDto} to which the error message will be set.
	 * @param e The {@link Exception} that contains the error details.
	 * @param status The {@link HttpStatus} to be associated with the error.
	 */
	private void setErrorToReturn(String function, ImportResponseDto importResponseDto, Exception e,
			HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append(function).append(Constants.ERROR_INIT2);

		setErrorToReturn(function, importResponseDto, e.getMessage(), e.getLocalizedMessage(),
				Arrays.toString(e.getStackTrace()), status);
	}
	
	
	/**
	 * Sets an error message and additional details on the {@link ImportResponseDto} and logs the error.
	 *
	 * <p>This method constructs an error message and sets various details on the {@link ImportResponseDto} including
	 * the error message, a custom message, a stack trace, and the HTTP status. It also logs these details for debugging purposes.</p>
	 *
	 * @param function The name of the function where the error occurred. Used to construct the error message.
	 * @param importResponseDto The {@link ImportResponseDto} to which the error details will be set.
	 * @param errorMessage The main error message to be set in the response.
	 * @param message A custom message providing additional context about the error.
	 * @param trace The stack trace of the exception, converted to a string for debugging purposes.
	 * @param status The {@link HttpStatus} to be associated with the error response.
	 */
	private void setErrorToReturn(String function, ImportResponseDto importResponseDto, String errorMessage,
			String message, String trace, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append(function).append(Constants.ERROR_INIT2);

		importResponseDto.setTimestamp(LocalDateTime.now());
		CapabilityLogger.logError(errorData.toString() + " Status: " + status);
		importResponseDto.setStatus(status);
		CapabilityLogger.logError(errorData.toString() + " ERROR: " + errorMessage);
		importResponseDto.setError(errorMessage);
		CapabilityLogger.logError(errorData.toString() + " MESSAGE: " + message);
		importResponseDto.setMessage(message);
		CapabilityLogger.logError(errorData.toString() + " TRACE: " + trace);
		importResponseDto.setTrace(trace);
	}

	/*private void setRolL1(FormDataImport formDataImport) {
		switch (formDataImport.getRolL1extendido()) {
		case Constants.ROLL1EX_SE:
			formDataImport.setVcProfileRolL1(Constants.ROLL1_SE);
			break;
		case Constants.ROLL1EX_BA:
			formDataImport.setVcProfileRolL1(Constants.ROLL1_BA);
			break;
		case Constants.ROLL1EX_EM, Constants.ROLL1EX_EM_PMO:
			formDataImport.setVcProfileRolL1(Constants.ROLL1_EM);
		break;
		case Constants.ROLL1EX_AR:
			formDataImport.setVcProfileRolL1(Constants.ROLL1_AR);
			break;
		default:
			formDataImport.setVcProfileRolL1(Constants.EMPTY);
		}
	}*/
	
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
	
}
