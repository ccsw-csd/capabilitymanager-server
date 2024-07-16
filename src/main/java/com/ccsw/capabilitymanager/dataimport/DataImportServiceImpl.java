package com.ccsw.capabilitymanager.dataimport;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccsw.capabilitymanager.activity.model.Activity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.S3Service.s3Service;
import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesActividadDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesActividadDataImport;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.itinerariosdataimport.ItinerariosActividadDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.ItinerariosDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.model.ItinerariosActividadDataImport;
import com.ccsw.capabilitymanager.itinerariosdataimport.model.ItinerariosDataImport;
import com.ccsw.capabilitymanager.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.capabilitymanager.staffingdataimport.model.StaffingDataImport;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapatidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.versionitinerarios.VersionItinerariosRepository;
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;
import com.ccsw.capabilitymanager.activitydataimport.ActivityDataImportRepository;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImportDto;
import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.activity.ActivityRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DataImportServiceImpl implements DataImportService {
	private static final Logger logger = LoggerFactory.getLogger(DataImportServiceImpl.class);

	@Autowired
	private FormDataImportRepository formDataImportRepository;

	@Autowired
	private StaffingDataImportRepository staffingDataImportRepository;

	@Autowired
	private CertificatesDataImportRepository certificatesDataImportRepository;
	
	@Autowired
	private CertificatesActividadDataImportRepository certificatesActividadDataImportRepository;
	
	@Autowired
	private ItinerariosDataImportRepository itinerariosDataImportRepository;
	
	@Autowired
	private ItinerariosActividadDataImportRepository itinerariosActividadDataImportRepository;

	@Autowired
	private VersionCapatidadesRepository versionCapatidadesRepository;

	@Autowired
	private VersionStaffingRepository versionStaffingRepository;
	
	@Autowired
	private VersionItinerariosRepository versionItinerariosRepository;

	@Autowired
	private VersionCertificacionesRepository versionCertificacionesRepository;

	@Autowired
	DataImportService service;

	@Autowired
	private UtilsServiceImpl utilsServiceImpl;

	@Autowired
	private DataserviceS3 dataservice;

	@Autowired
	private s3Service s3service;

	@Autowired
	private ActivityDataImportRepository activityDataImportRepository;

	@Autowired
	private ActivityRepository activityRepository;


	@Override
	public ImportResponseDto processObject(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processObject ");
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

		logger.debug("[DataImportServiceImpl] processObject >>>>");
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
		logger.debug(" >>>> processRolsDoc ");
		var importResponseDto = new ImportResponseDto();
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
		List<FormDataImport> formDataImportList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		FormDataImport data = new FormDataImport();
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new FormDataImport();
			String vcProfileSAGA = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_SAGA.getPosition());
			String vcProfileEmail = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_EMAIL.getPosition());
			String vcProfileName = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_NAME.getPosition());
			String vcProfileRoll1Extendido = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition());
			String vcProfileRoll2EM = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL2_EM.getPosition());
			String vcProfileRoll2AR = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL2_AR.getPosition());
			String vcProfileRoll2AN = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL2_AN.getPosition());
			String vcProfileRoll2SE = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL2_SE.getPosition());
			String vcProfileRolExperienceEM = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_EM.getPosition());
			String vcProfileRolExperienceAR = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROL_EXPERIENCE_AR.getPosition());
			String vcProfileRoll3 = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL3.getPosition());
			String vcProfileSkillCloudNativeExperience = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_SKILL_CLOUD_NATIVE_EXPERIENCE.getPosition());
			String vcProfileSkillLowCodeExperience = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_SKILLL_OWCODE_EXPERIENCE.getPosition());
			String vcProfileRoll4 = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_ROLL4.getPosition());
			String vcProfileSectorExperience = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_SECTOR_EXPERIENCE.getPosition());
			String vcProfileSkillCloudExp = utilsServiceImpl.getStringValue(currentRow,
					Constants.RolsDatabasePos.COL_SKILL_CLOUD_EXPERIENCE.getPosition());

			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(vcProfileSAGA, "Saga");


		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	ImportResponseDto error = new ImportResponseDto();
                	error.setStatus(HttpStatus.BAD_REQUEST);
                	error.setMessage("Row " + i + " is missing required field: " + entry.getValue());
                	error.setError("Row " + i + " is missing required field: " + entry.getValue());
                    return error;
                }
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
			data.setNumImportCodeId(verCap.getId());
			setRolL1(data);

			formDataImportList.add(data);
			currentRow = sheet.getRow(i);
			data = new FormDataImport();
		}

		if (formDataImportList != null && !formDataImportList.isEmpty()) {
			saveAllFormDataImport(formDataImportList);
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_ROL_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_ROL_FILE);
		}

		logger.debug("      processRolsDoc >>>>");
		return importResponseDto;

	}

	/**
	 * Process Staffing Document received
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	@Transactional
	private ImportResponseDto processStaffingDoc(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processStaffingDoc ");

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
			String ultimoCampo = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PRACTICE_AREA.getPosition());

			Map<String, String> noNulables = new HashMap<>();
	        noNulables.put(saga, "Saga");
	        noNulables.put(ggid, "Ggid");
	        noNulables.put(centro, "Centro");
	        noNulables.put(nombre, "Nombre");
	        noNulables.put(ultimoCampo, "No llegan todos los campos");


		       // Verificar si alguno de los campos es nulo o está vacío
            for (Map.Entry<String, String> entry : noNulables.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) {
                	ImportResponseDto error = new ImportResponseDto();
                	error.setStatus(HttpStatus.BAD_REQUEST);
                	error.setMessage("Row " + i + " is missing required field: " + entry.getValue());
                	error.setError("Row " + i + " is missing required field: " + entry.getValue());
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

			staffingDataImportList.add(data);
			currentRow = sheet.getRow(i);
		}

		if (staffingDataImportList != null && !staffingDataImportList.isEmpty()) {
			saveAllStaffingDataImport(staffingDataImportList);

		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		logger.debug(" [DataImportServiceImpl]      processStaffingDoc >>>>");
		return importResponseDto;

	}

	/**
	 * Process Certification Document received (waitting specificatios)
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	private ImportResponseDto processCertificatesDoc(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
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
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		logger.debug("[DataImportServiceImpl]       processCertificatesDoc >>>>");
		return importResponseDto;
	}
	
	/**
	 * Process Itinerarios Document received (waitting specificatios)
	 *
	 * @param dto ImportRequestDto Object
	 * @return ImportResponseDto Object
	 */
	private ImportResponseDto processItinerariosDoc(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
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
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		logger.debug("[DataImportServiceImpl]       processItinerariosDoc >>>>");
		return importResponseDto;
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
		versionCap.setIdTipointerfaz(Integer.valueOf(idTipointerfaz));
		versionCap.setFichero(dataservice.getS3Endpoint());

		return versionCapatidadesRepository.save(versionCap);
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
		versionStaf.setIdTipointerfaz(Integer.valueOf(idTipointerfaz));
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
		versionCer.setIdTipointerfaz(Integer.valueOf(dto.getDocumentType()));
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
	private List<FormDataImport> saveAllFormDataImport(List<FormDataImport> formDataImportList) {
//			VersionCapacidades verCap) {
		try {
			return formDataImportRepository.saveAll(formDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
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
			logger.error(errorData.toString() + e.getMessage());
			String respuestaEr = "Error procesando el excel.Comprueba los datos correctos";
			throw new UnprocessableEntityException(respuestaEr);
		}
	}


	private void setErrorToReturn(String function, ImportResponseDto importResponseDto, Exception e,
			HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append(function).append(Constants.ERROR_INIT2);

		setErrorToReturn(function, importResponseDto, e.getMessage(), e.getLocalizedMessage(),
				Arrays.toString(e.getStackTrace()), status);
	}

	private void setErrorToReturn(String function, ImportResponseDto importResponseDto, String errorMessage,
			String message, String trace, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append(function).append(Constants.ERROR_INIT2);

		importResponseDto.setTimestamp(LocalDateTime.now());
		logger.error(errorData.toString() + " Status: " + status);
		importResponseDto.setStatus(status);
		logger.error(errorData.toString() + " ERROR: " + errorMessage);
		importResponseDto.setError(errorMessage);
		logger.error(errorData.toString() + " MESSAGE: " + message);
		importResponseDto.setMessage(message);
		logger.error(errorData.toString() + " TRACE: " + trace);
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
