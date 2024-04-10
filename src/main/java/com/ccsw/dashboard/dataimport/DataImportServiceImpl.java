package com.ccsw.dashboard.dataimport;

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

import com.ccsw.dashboard.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.dashboard.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.common.exception.UnprocessableEntityException;
import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;
import com.ccsw.dashboard.formdataimport.FormDataImportRepository;
import com.ccsw.dashboard.formdataimport.model.FormDataImport;
import com.ccsw.dashboard.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.dashboard.staffingdataimport.model.StaffingDataImport;
import com.ccsw.dashboard.utils.UtilsServiceImpl;
import com.ccsw.dashboard.versioncapacidades.VersionCapatidadesRepository;
import com.ccsw.dashboard.versioncapacidades.model.VersionCapacidades;
import com.ccsw.dashboard.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.dashboard.versioncertificados.model.VersionCertificaciones;
import com.ccsw.dashboard.versionstaffing.VersionStaffingRepository;
import com.ccsw.dashboard.versionstaffing.model.VersionStaffing;

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
	private UtilsServiceImpl utilsServiceImpl;
	
	@Override
	public ImportResponseDto processObject(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processObject ");
		utilsServiceImpl.checkInputObject(dto);
		ImportResponseDto importResponseDto = new ImportResponseDto();
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
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(),importResponseDto, 
					Constants.ERROR_DOCUMENT_TYPE, Constants.ERROR_DOCUMENT_TYPE, Constants.EMPTY, HttpStatus.BAD_REQUEST);
		}
		logger.debug("[DataImportServiceImpl]       processObject >>>>");
		return importResponseDto;

	}
	
	/**
	 * Process Rol Document received
	 * @param dto  	ImportRequestDto Object
	 * @return 		ImportResponseDto Object
	 */
	@Transactional
	private ImportResponseDto processRolsDoc(ImportRequestDto dto) {
		logger.debug(" >>>> processRolsDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		List<FormDataImport> formDataImportList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		int sizeSheet = sheet.getPhysicalNumberOfRows() -1;
		VersionCapacidades verCap = null;
		try {
			verCap = createCapacityVersion(sizeSheet,dto.getFileData().getOriginalFilename(), dto.getDescription(),dto.getUser(), 
					dto.getDocumentType(), dto.getFileData().getBytes());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		FormDataImport data = new FormDataImport();
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new FormDataImport();
			String vcProfileSAGA = utilsServiceImpl.getStringValue (currentRow, Constants.RolsDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileEmail = utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEEMAIL.getPosition());
			String vcProfileName =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILENAME.getPosition());
			String vcProfileRoll1Extendido =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL1EXTENDIDO.getPosition());
			String vcProfileRoll2EM =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2EM.getPosition());
			String vcProfileRoll2AR =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2AR.getPosition());
			String vcProfileRoll2AN =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2AN.getPosition());
			String vcProfileRoll2SE =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2SE.getPosition());
			String vcProfileRolExperienceEM =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEEM.getPosition());
			String vcProfileRolExperienceAR =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEAR.getPosition());
			String vcProfileRoll3 =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL3.getPosition());
			String vcProfileSkillCloudNativeExperience =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLCLOUDNATIVEEXPERIENCE.getPosition());
			String vcProfileSkillLowCodeExperience =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLLOWCODEEXPERIENCE.getPosition());		
			String vcProfileRoll4 =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL4.getPosition());
			String vcProfileSectorExperience =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESECTOREXPERIENCE.getPosition());
			String vcProfileSkillCloudExp =  utilsServiceImpl.getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLCLOUDEXP.getPosition());

			data.setVcProfileSAGA(vcProfileSAGA);
			data.setVcProfileEmail(vcProfileEmail);
			data.setVcProfileName(vcProfileName);
			data.setVcProfileRolL1extendido(vcProfileRoll1Extendido);
			data.setVcProfileRolL2EM(vcProfileRoll2EM);
			data.setVcProfileRolL2AR(vcProfileRoll2AR);
			data.setVcProfileRolL2AN(vcProfileRoll2AN);
			data.setVcProfileRolL2SE(vcProfileRoll2SE);
			data.setVcProfileRolExperienceEM(vcProfileRolExperienceEM);
			data.setVcProfileRolExperienceAR(vcProfileRolExperienceAR);
			data.setVcProfileRolL3(vcProfileRoll3);
			data.setVcProfileRolL4(vcProfileRoll4);
			data.setVcProfileSkillCloudNativeExperience(vcProfileSkillCloudNativeExperience);
			data.setVcProfileSkillLowCodeExperience(vcProfileSkillLowCodeExperience);
			data.setVcProfileSectorExperience(vcProfileSectorExperience);
			data.setVcProfileSkillCloudExp(vcProfileSkillCloudExp);

			data.setNumImportCodeId(verCap.getId());
			setVcProfileRolL1(data);
			
			formDataImportList.add(data);
			currentRow = sheet.getRow(i);
			data = new FormDataImport();
		}

		if (formDataImportList != null && !formDataImportList.isEmpty()) {
				saveAllFormDataImport(formDataImportList, verCap);
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT)
			.append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_ROL_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_ROL_FILE);
		}

		logger.debug("      processRolsDoc >>>>");
		return importResponseDto;

	}

	/**
	 * Process Staffing Document received
	 * @param dto 	ImportRequestDto Object
	 * @return 		ImportResponseDto Object
	 */
	@Transactional
	private ImportResponseDto processStaffingDoc(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processStaffingDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int sizeSheet = sheet.getPhysicalNumberOfRows() -1;
		VersionStaffing verStaf = null;
		try {
			verStaf = createStaffingVersion(sizeSheet,dto.getFileData().getOriginalFilename(), dto.getDescription(), dto.getUser(), 
					dto.getDocumentType(), dto.getFileData().getBytes());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		
		List<StaffingDataImport> staffingDataImportList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		StaffingDataImport data = new StaffingDataImport();
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new StaffingDataImport();
			String vcProfileSAGA = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileGGID = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEGGID.getPosition());
			String vcProfileCentro = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECENTRO.getPosition());
			String vcProfileNombre = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILENOMBRE.getPosition());
			String vcProfileApellidos = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEAPELLIDOS.getPosition());
			String vcProfileLocalizacion = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILELOCALIZACION.getPosition());
			String vcProfilePractica = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPRACTICA.getPosition());
			String vcProfileGrado = utilsServiceImpl.getGradeValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEGRADO.getPosition());
			String vcProfileCategoria = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECATEGORIA.getPosition());
			String vcProfilePerfilTecnico = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPERFILTECNICO.getPosition());
			Date vcProfileFechaIncorporacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAINCORPORACION.getPosition());
			String vcProfilePorcentajeAsignacion = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPORCENTAJEASIGNACION.getPosition());
			String vcProfileStatus = utilsServiceImpl.getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILESTATUS.getPosition());
			String vcProfileClienteActual = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECLIENTEACTUAL.getPosition());
			Date vcProfileFechaInicioAsignacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAINICIOASIGNACION.getPosition());
			Date vcProfileFechaFinAsignacion = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAFINASIGNACION.getPosition());
			Date vcProfileFechaDisponibilidad = utilsServiceImpl.getDateValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHADISPONIBILIDAD.getPosition());
			String vcProfilePosicionProyectoFuturo = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPOSICIONPROYECTOFUTURO.getPosition());
			String vcProfileColaboraciones = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECOLABORACIONES.getPosition());
			String vcProfileProyectoAnterior = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COLVCPROFILEPROYECTOANTERIOR.getPosition());
			String vcProfileMesesBench = utilsServiceImpl.getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEMESESBENCH.getPosition());

			data.setVcProfileSAGA(vcProfileSAGA);
			data.setVcProfileGGID(vcProfileGGID);
			data.setVcProfileCentro(vcProfileCentro);
			data.setVcProfileNombre(vcProfileNombre);
			data.setVcProfileApellidos(vcProfileApellidos);
			data.setVcProfileLocalizacion(vcProfileLocalizacion);
			data.setVcProfilePractica(vcProfilePractica);
			data.setVcProfileGrado(vcProfileGrado);
			data.setVcProfileCategoria(vcProfileCategoria);
			data.setVcProfilePerfilTecnico(vcProfilePerfilTecnico);
			data.setVcProfileFechaIncorporacion(vcProfileFechaIncorporacion);
			data.setVcProfileAsignacion(vcProfilePorcentajeAsignacion);
			data.setVcProfileStatus(vcProfileStatus);
			data.setVcProfileClienteActual(vcProfileClienteActual);
			data.setVcProfileFechaInicioAsignacion(vcProfileFechaInicioAsignacion);
			data.setVcProfileFechaFinAsignacion(vcProfileFechaFinAsignacion);
			data.setVcProfileFechaDisponibilidad(vcProfileFechaDisponibilidad);
			data.setVcProfileProyectoFuturo(vcProfilePosicionProyectoFuturo);
			data.setVcProfileColaboraciones(vcProfileColaboraciones);
			data.setVcProfileProyectoAnterior(vcProfileProyectoAnterior);
			data.setVcProfileMesesBench(vcProfileMesesBench);
			data.setNumImportCodeID(verStaf.getId());
			
			staffingDataImportList.add(data);
			currentRow = sheet.getRow(i);
		}

		if (staffingDataImportList != null && !staffingDataImportList.isEmpty()) {
			saveAllStaffingDataImport(staffingDataImportList);
			
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT)
			.append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		logger.debug(" [DataImportServiceImpl]      processStaffingDoc >>>>");
		return importResponseDto;

	}

	/**
	 * Process Certification Document received (waitting specificatios)
	 * @param dto 	ImportRequestDto Object
	 * @return 		ImportResponseDto Object
	 */
	private ImportResponseDto processCertificatesDoc(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processCertificatesDoc ");
		ImportResponseDto importResponseDto = new ImportResponseDto();

		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		VersionCertificaciones verCerytificaciones = null;
		try {
			verCerytificaciones = createCertificationesVersion(dto);
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		
		List<CertificatesDataImport> listCertificacionesDataImport = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			String vcSAGA = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSAGA.getPosition());
			String vcPartner = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCPARTNER.getPosition());
			String vcCertificado = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICADO.getPosition());
			String vcNameGTD = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCNAMEGTD.getPosition());
			String vcCertificationGDT = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICATIONGTD.getPosition());
			String vcCode =  utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCODE.getPosition());
			String vcSector = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSECTOR.getPosition());
			String vcModulo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCMODULO.getPosition());
			String vcIdCandidato = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCIDCANDIDATO.getPosition());
			Date vcFechaCertificado = utilsServiceImpl.getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHACERTIFICADO.getPosition());
			Date vcFechaExpiracion = utilsServiceImpl.getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHAEXPIRACION.getPosition());
			String vcActivo = utilsServiceImpl.getStringValue (currentRow, Constants.CertificatesDatabasePos.COL_VCACTIVO.getPosition());
			String vcAnexo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCANEXO.getPosition());
			String vcComentarioAnexo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCOMENTARIOANEXO.getPosition());
			
			data.setVcSAGA(vcSAGA);
			data.setVcPartner(vcPartner);
			data.setVcCertificado(vcCertificado);
			data.setVcNameGTD(vcNameGTD);
			data.setVcCertificationGTD(vcCertificationGDT);
			data.setVcCode(vcCode);
			data.setVcSector(vcSector);
			data.setVcModulo(vcModulo);
			data.setVcIdCandidato(vcIdCandidato);
			data.setVcFechaCertificado(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			data.setVcFechaExpiracion(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			data.setVcActivo(vcActivo);
			data.setVcAnexo(vcAnexo);
			data.setVcComentarioAnexo(vcComentarioAnexo);

			data.setNumImportCode(verCerytificaciones.getId());
			
			if(!data.getVcSAGA().isEmpty()) {
				listCertificacionesDataImport.add(data);
			}
			currentRow = sheet.getRow(i);
		}
		
		if (listCertificacionesDataImport != null && !listCertificacionesDataImport.isEmpty()) {
			saveAllCertificatesDataImport(listCertificacionesDataImport);
			
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT)
			.append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		logger.debug("[DataImportServiceImpl]       processCertificatesDoc >>>>");
		return importResponseDto;
	}


	
	/**
	 * Create an save on database CapacityVersion Object
	 * @param numReg			num registers on Excel
	 * @param fileName			Excell File name 
	 * @param description		Description
	 * @param user				User who uploads data
	 * @param idTipointerfaz	idTipointerfaz value	
	 * @param bs				File in byte array
	 * @return	CapacityVersion Object inserted on database
	 */
	private VersionCapacidades createCapacityVersion(int numReg,  String fileName, String description, 
			String user, String idTipointerfaz, byte[] bs) {
		VersionCapacidades versionCap = new VersionCapacidades();
		versionCap.setNumRegistros(numReg);
		versionCap.setFechaImportacion(LocalDateTime.now());
		versionCap.setNombreFichero(fileName);
		versionCap.setDescripcion(description);
		versionCap.setUsuario(user);
		versionCap.setIdTipointerfaz(Integer.valueOf(idTipointerfaz));
		versionCap.setFichero(bs);
		
		return versionCapatidadesRepository.save(versionCap);
	}

	/**
	 * Create an save on database VersionStaffing Object
	 * @param numReg			num registers on Excel
	 * @param fileName			Excell File name 
	 * @param description		Description
	 * @param user				User who uploads data
	 * @param idTipointerfaz	idTipointerfaz value	
	 * @param bs				File in byte array
	 * @return	VersionStaffing Object inserted on database
	 */
	private VersionStaffing createStaffingVersion(int numReg, String fileName, String description, 
			String user, String idTipointerfaz, byte[] bs) {
		VersionStaffing versionStaf = new VersionStaffing();
		versionStaf.setNumRegistros(numReg);
		versionStaf.setFechaImportacion(LocalDateTime.now());
		versionStaf.setNombreFichero(fileName);
		versionStaf.setDescripcion(description);
		versionStaf.setUsuario(user);
		versionStaf.setIdTipointerfaz(Integer.valueOf(idTipointerfaz));
		versionStaf.setFichero(bs);
		
		return versionStaffingRepository.save(versionStaf);
	}

	/**
	 * Create an save on database CertificationsVersion Object (with CertificatesDataImport relations)
	 * @param numReg			num registers on Excel
	 * @param fileName			Excell File name 
	 * @param description		Description
	 * @param user				User who uploads data
	 * @param idTipointerfaz	idTipointerfaz value	
	 * @param bs				File in byte array
	 * @return	CapacityVersion Object inserted on database
	 * @throws IOException 
	 */
	private VersionCertificaciones createCertificationesVersion( ImportRequestDto dto) throws IOException {
		Sheet sheet = utilsServiceImpl.obtainSheet(dto.getFileData());
		int numReg = sheet.getPhysicalNumberOfRows() -1;
		
		VersionCertificaciones versionCer = new VersionCertificaciones();
		versionCer.setIdTipointerfaz(Integer.valueOf(dto.getDocumentType()));
		versionCer.setFechaImportacion(LocalDateTime.now());
		versionCer.setNumRegistros(numReg);
		versionCer.setNombreFichero(dto.getFileData().getOriginalFilename());
		versionCer.setDescription(dto.getDescription());
		versionCer.setUsuario(dto.getUser());
		versionCer.setFichero(dto.getFileData().getBytes());
//		versionCer.setCertificates(setCertificacionesDataImport(versionCer, sheet));
		
		return versionCertificacionesRepository.save(versionCer);
	}
	/**
	 * construct Set<CertificatesDataImport> related with VersionCertificaciones
	 * @param sheet document to 
	 * @return
	 * /
	private Set<CertificatesDataImport> setCertificacionesDataImport (VersionCertificaciones versionCer, Sheet sheet){
		Set<CertificatesDataImport> setCertificatesDataImportObject = new HashSet<CertificatesDataImport>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			String vcSAGA = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSAGA.getPosition());
			String vcPartner = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCPARTNER.getPosition());
			String vcCertificado = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICADO.getPosition());
			String vcNameGTD = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCNAMEGTD.getPosition());
			String vcCertificationGDT = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICATIONGTD.getPosition());
			String vcCode =  utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCODE.getPosition());
			String vcSector = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSECTOR.getPosition());
			String vcModulo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCMODULO.getPosition());
			String vcIdCandidato = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCIDCANDIDATO.getPosition());
			Date vcFechaCertificado = utilsServiceImpl.getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHACERTIFICADO.getPosition());
			Date vcFechaExpiracion = utilsServiceImpl.getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHAEXPIRACION.getPosition());
			String vcActivo = utilsServiceImpl.getStringValue (currentRow, Constants.CertificatesDatabasePos.COL_VCACTIVO.getPosition());
			String vcAnexo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCANEXO.getPosition());
			String vcComentarioAnexo = utilsServiceImpl.getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCOMENTARIOANEXO.getPosition());

//			data.setNum_import_code_id(versionCer.getId());
			data.setVcSAGA(vcSAGA);
			data.setVcPartner(vcPartner);
			data.setVcCertificado(vcCertificado);
			data.setVcNameGTD(vcNameGTD);
			data.setVcCertificationGTD(vcCertificationGDT);
			data.setVcCode(vcCode);
			data.setVcSector(vcSector);
			data.setVcModulo(vcModulo);
			data.setVcIdCandidato(vcIdCandidato);
			data.setVcFechaCertificado(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			data.setVcFechaExpiracion(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			data.setVcActivo(vcActivo);
			data.setVcAnexo(vcAnexo);
			data.setVcComentarioAnexo(vcComentarioAnexo);
			
			if(!data.getVcSAGA().equals(Constants.EMPTY)) {
				setCertificatesDataImportObject.add(data);
			}
			currentRow = sheet.getRow(i);
		}
		
		if (setCertificatesDataImportObject != null && !setCertificatesDataImportObject.isEmpty()) {
			saveAllCertificatesDataImport(setCertificatesDataImportObject);
			
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT)
			.append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(Constants.ERROR_EMPTY_STAFFING_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(Constants.ERROR_EMPTY_STAFFING_FILE);
		}

		return setCertificatesDataImportObject;
	}*/

	/**
	 * Save list FormDataImport on database
	 * @param formDataImportList	List Object FormDataImport
	 * @return	List<FormDataImport>
	 */
	@Transactional
	private List<FormDataImport> saveAllFormDataImport(List<FormDataImport> formDataImportList, VersionCapacidades verCap) {
        try {
        	return (List<FormDataImport>) formDataImportRepository.saveAll(formDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
			throw new UnprocessableEntityException(e.getMessage());
		}
    }
	
	/**
	 * Save list StaffingDataImport on database
	 * @param staffingDataImportList	List Object StaffingDataImport
	 * @return	List<StaffingDataImport>
	 */
	@Transactional
	private List<StaffingDataImport> saveAllStaffingDataImport(List<StaffingDataImport> staffingDataImportList) {
		try {
			return (List<StaffingDataImport>) staffingDataImportRepository.saveAll(staffingDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
			throw new UnprocessableEntityException(errorData.toString());
		}
    }

	/**
	 * Save list CertificatesDataImport on database
	 * @param certificatesDataImportList List of objects to save
	 * @return List<CertificatesDataImport>
	 */
	@Transactional
	private List<CertificatesDataImport> saveAllCertificatesDataImport(List<CertificatesDataImport> certificatesDataImportList) {
		try {
			return (List<CertificatesDataImport>) certificatesDataImportRepository.saveAll(certificatesDataImportList);
		} catch (Exception e) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
			throw new UnprocessableEntityException(errorData.toString());
		}
    }

	private  void setErrorToReturn( String function, ImportResponseDto importResponseDto, Exception e, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append( function ).append(Constants.ERROR_INIT2);
		
		setErrorToReturn(function, importResponseDto, e.getMessage(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), status);
    }
	
	private  void setErrorToReturn( String function, ImportResponseDto importResponseDto, String errorMessage , String message, String trace, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(Constants.ERROR_INIT).append( function ).append(Constants.ERROR_INIT2);
		
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
	
	private void setVcProfileRolL1(FormDataImport formDataImport) {
		switch (formDataImport.getVcProfileRolL1extendido()) {
			case Constants.VCPROFILEROLL1EX_OP1:
				formDataImport.setVcProfileRolL1(Constants.VCPROFILEROLL1_OP1);
				break;
			case Constants.VCPROFILEROLL1EX_OP2:
				formDataImport.setVcProfileRolL1(Constants.VCPROFILEROLL1_OP2);
				break;
			case Constants.VCPROFILEROLL1EX_OP3, Constants.VCPROFILEROLL1EX_OP3_2:
				formDataImport.setVcProfileRolL1(Constants.VCPROFILEROLL1_OP3);
				break;
			case Constants.VCPROFILEROLL1EX_OP4:
				formDataImport.setVcProfileRolL1(Constants.VCPROFILEROLL1_OP4);
				break;
			default:
				formDataImport.setVcProfileRolL1(Constants.EMPTY);
		}
	}
}
