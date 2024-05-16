package com.ccsw.capabilitymanager.dataimport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.S3Service.s3Service;
import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.capabilitymanager.staffingdataimport.model.StaffingDataImport;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapatidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

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
	private VersionCapatidadesRepository versionCapatidadesRepository;

	@Autowired
	private VersionStaffingRepository versionStaffingRepository;

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
			String porcentajeAsignacion = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_PORCENTAJE_ASIGNACION.getPosition());
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
			String vcProfileMesesBench = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_MESES_BENCH.getPosition());

			data.setNumImportCodeID(verStaf.getId());
			data.setSAGA(saga);
			data.setGGID(ggid);
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
			data.setProyectoFuturo(posicionProyectoFuturo);
			data.setColaboraciones(colaboraciones);
			data.setProyectoAnterior(proyectoAnterior);
			data.setInglesEscrito(inglesEscrito);
			data.setInglesHablado(inglesHablado);
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
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
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
			if (!data.getSAGA().isEmpty()) {
				listCertificacionesDataImport.add(data);
			}
			currentRow = sheet.getRow(i);
		}

		if (listCertificacionesDataImport != null && !listCertificacionesDataImport.isEmpty()) {
			saveAllCertificatesDataImport(listCertificacionesDataImport);

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
			throw new UnprocessableEntityException(e.getMessage());
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
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
			.append(Constants.ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
			throw new UnprocessableEntityException(errorData.toString());
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
			throw new UnprocessableEntityException(errorData.toString());
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

	private void setRolL1(FormDataImport formDataImport) {
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
	}

}
