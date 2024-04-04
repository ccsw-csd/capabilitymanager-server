package com.ccsw.dashboard.dataimport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.common.RowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.common.exception.BadRequestException;
import com.ccsw.dashboard.common.exception.UnprocessableEntityException;
import com.ccsw.dashboard.common.exception.UnsupportedMediaTypeException;
import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;
import com.ccsw.dashboard.formdataimport.FormDataImportRepository;
import com.ccsw.dashboard.formdataimport.model.FormDataImport;
import com.ccsw.dashboard.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.dashboard.staffingdataimport.model.StaffingDataImport;
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
	private VersionCapatidadesRepository versionCapatidadesRepository;
	
	@Autowired
	private VersionStaffingRepository versionStaffingRepository;
	
	@Autowired
	private VersionCertificacionesRepository versionCertificacionesSRepository;
	
	@Override
	public ImportResponseDto processObject(ImportRequestDto dto) {
		logger.debug("[DataImportServiceImpl]  >>>> processObject ");
		checkInputObject(dto);
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

		Sheet sheet = obtainSheet(dto.getFileData());
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
			String vcProfileSAGA = getStringValue (currentRow, Constants.RolsDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileEmail = getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEEMAIL.getPosition());
			String vcProfileName =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILENAME.getPosition());
			String vcProfileRoll1Extendido =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL1EXTENDIDO.getPosition());
			String vcProfileRoll2EM =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2EM.getPosition());
			String vcProfileRoll2AR =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2AR.getPosition());
			String vcProfileRoll2AN =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2AN.getPosition());
			String vcProfileRoll2SE =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL2SE.getPosition());
			String vcProfileRolExperienceEM =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEEM.getPosition());
			String vcProfileRolExperienceAR =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEAR.getPosition());
			String vcProfileRoll3 =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL3.getPosition());
			String vcProfileSkillCloudNativeExperience =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLCLOUDNATIVEEXPERIENCE.getPosition());
			String vcProfileSkillLowCodeExperience =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLLOWCODEEXPERIENCE.getPosition());		
			String vcProfileRoll4 =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILEROLL4.getPosition());
			String vcProfileSectorExperience =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESECTOREXPERIENCE.getPosition());
			String vcProfileSkillCloudExp =  getStringValue(currentRow, Constants.RolsDatabasePos.COL_VCPROFILESKILLCLOUDEXP.getPosition());

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

		Sheet sheet = obtainSheet(dto.getFileData());
		List<StaffingDataImport> staffingDataImportList = new ArrayList<>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		int sizeSheet = sheet.getPhysicalNumberOfRows() -1;
		VersionStaffing verStaf = null;
		try {
			verStaf = createStaffingVersion(sizeSheet,dto.getFileData().getOriginalFilename(), dto.getDescription(), dto.getUser(), 
					dto.getDocumentType(), dto.getFileData().getBytes());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		
		StaffingDataImport data = new StaffingDataImport();
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new StaffingDataImport();
			String vcProfileSAGA = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileGGID = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEGGID.getPosition());
			String vcProfileNombre = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILENOMBRE.getPosition());
			String vcProfileApellidos = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEAPELLIDOS.getPosition());
			String vcProfilePractica = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPRACTICA.getPosition());
			String vcProfileGrado = getGradeValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEGRADO.getPosition());
			String vcProfileCategoria = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECATEGORIA.getPosition());
			String vcProfileCentro = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECENTRO.getPosition());
			String vcProfileLocalizacion = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILELOCALIZACION.getPosition());
			String vcProfilePerfilTecnico = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPERFILTECNICO.getPosition());
			String vcProfileFechaIncorporacion = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAINCORPORACION.getPosition());
			String vcProfilePorcentajeAsignacion = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPORCENTAJEASIGNACION.getPosition());
			String vcProfileStatus = getStringValue (currentRow, Constants.StaffingDatabasePos.COL_VCPROFILESTATUS.getPosition());
			String vcProfileClienteActual = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECLIENTEACTUAL.getPosition());
			String vcProfileFechaInicioAsignacion = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAINICIOASIGNACION.getPosition());
			String vcProfileFechaFinAsignacion = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHAFINASIGNACION.getPosition());
			String vcProfileFechaDisponibilidad = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEFECHADISPONIBILIDAD.getPosition());
			String vcProfilePosicionProyectoFuturo = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEPOSICIONPROYECTOFUTURO.getPosition());
			String vcProfileColaboraciones = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILECOLABORACIONES.getPosition());
			String vcProfileProyectoAnterior = getStringValue(currentRow, Constants.StaffingDatabasePos.COLVCPROFILEPROYECTOANTERIOR.getPosition());
			String vcProfileMesesBench = getStringValue(currentRow, Constants.StaffingDatabasePos.COL_VCPROFILEMESESBENCH.getPosition());

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
			data.setVcProfileDisponibilidad(vcProfileFechaDisponibilidad);
			data.setVcProfileProyectoFuturo(vcProfilePosicionProyectoFuturo);
			data.setVcProfileColaboraciones(vcProfileColaboraciones);
			data.setVcProfileProyectoAnterior(vcProfileProyectoAnterior);
			data.setVcProfileMesesBench(vcProfileMesesBench);
			data.setNumImportCodeID(verStaf.getId());
			
			staffingDataImportList.add(data);
			currentRow = sheet.getRow(i);
		}

		if (staffingDataImportList != null && !staffingDataImportList.isEmpty()) {
			saveAllStaffingDataImport(staffingDataImportList,verStaf);
			
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

		VersionCertificaciones verCerytificaciones = null;
		try {
			verCerytificaciones = createCertificationsVersion(dto);
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		

		
		
		logger.debug("[DataImportServiceImpl]       processCertificatesDoc >>>>");
		return importResponseDto;
	}

	/**
	 * Get the main Excel tab given a file
	 * @param file 	Excel File
	 * @return 		selected Excel tab
	 * @throws BadRequestException It is not possible to read the provided file
	 */
	private Sheet obtainSheet(MultipartFile file) throws BadRequestException {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			return workbook.getSheetAt(Constants.FIRST_SHEET);
		} catch (Exception e) {
			throw new BadRequestException("An error occurred reading the file. Check the validity of the data and that it is not encrypted.");
		}
	}
	
	/**
	 * Check Input Object if ContentType is not valid or fileData is emptu or null get thow
	 * @param 	dto ImportRequestDto Object
	 * @throws 	UnsupportedMediaTypeException or UnprocessableEntityException
	 */
	private void checkInputObject(ImportRequestDto dto) {
		logger.debug(" >>>> checkInputObject ");
		if (dto.getFileData().getOriginalFilename() == Constants.EMPTY) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(" FileData is empty");
			logger.error(errorData.toString() );
			throw new UnsupportedMediaTypeException("FileData is empty");
		}
		if (!Constants.ALLOWED_FORMATS.contains(dto.getFileData().getContentType())) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append("FileData dont has valid format");
			logger.error(errorData.toString());
			throw new UnprocessableEntityException("FileData dont has valid format");
		}
		logger.debug("      checkInputObject >>>>");
	}
	
	/**
	 * Get String value from Row
	 * @param row		to recover value
	 * @param column	value to recover
	 * @return 			column value in string format
	 */
	private String getStringValue(Row row, int column) {
		String result = Constants.EMPTY;
		Cell col = row.getCell(column);
		if(col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = String.valueOf((int) col.getNumericCellValue());
			} else if (col.getCellType() == CellType.STRING){
				result = col.getStringCellValue();
			} else if(col.getCellType() == CellType.BOOLEAN) {
				result = String.valueOf(col.getBooleanCellValue());
			}
		}
		return result;
	}

	/**
	 * Get Date value from Row
	 * @param row		to recover date value
	 * @param column	value to recover
	 * @return 			column value in Date format
	 */
	private Date getDateValue(Row row, int column) {
		Date result = Constants.FUNDATIONDAYLESSONE;
		Cell col = row.getCell(column);
		if(col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = col.getDateCellValue(); 
			}
		}
		return result;
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
	private VersionCertificaciones createCertificationsVersion( ImportRequestDto dto) throws IOException {
		Sheet sheet = obtainSheet(dto.getFileData());
		int numReg = sheet.getPhysicalNumberOfRows() -1;
		
		VersionCertificaciones versionCer = new VersionCertificaciones();
		versionCer.setNumRegistros(numReg);
		versionCer.setFechaImportacion(LocalDateTime.now());
		versionCer.setNombreFichero(dto.getFileData().getOriginalFilename());
		versionCer.setDescription(dto.getDescription());
		versionCer.setUsuario(dto.getUser());
		versionCer.setIdTipointerfaz(Integer.valueOf(dto.getDocumentType()));
		versionCer.setFichero(dto.getFileData().getBytes());
		versionCer.setCertificates(setCertificacionesDataImport(sheet));
		
		return versionCertificacionesSRepository.save(versionCer);
	}
	/**
	 * construct Set<CertificatesDataImport> related with VersionCertificaciones
	 * @param sheet document to 
	 * @return
	 */
	private Set<CertificatesDataImport> setCertificacionesDataImport (Sheet sheet){
		Set<CertificatesDataImport> setCertificatesDataImportObject = new HashSet<CertificatesDataImport>();
		Row currentRow = sheet.getRow(Constants.ROW_EVIDENCE_LIST_START);
		CertificatesDataImport data = null;
		for (int i = Constants.ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new CertificatesDataImport();
			String vcActivo = getStringValue (currentRow, Constants.CertificatesDatabasePos.COL_VCACTIVO.getPosition());
			String vcAnexo = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCANEXO.getPosition());
			String vcCertificado = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICADO.getPosition());
			String vcCertificationGDT = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCERTIFICATIONGTD.getPosition());
			String vcCode =  getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCODE.getPosition());
			String vcComentarioAnexo = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCCOMENTARIOANEXO.getPosition());
			Date vcFechaCertificado = getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHACERTIFICADO.getPosition());
			Date vcFechaExpiracion = getDateValue(currentRow, Constants.CertificatesDatabasePos.COL_VCFECHAEXPIRACION.getPosition());
			String vcIdCandidato = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCIDCANDIDATO.getPosition());
			String vcModulo = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCMODULO.getPosition());
			String vcNameGTD = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCNAMEGTD.getPosition());
			String vcPartner = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCPARTNER.getPosition());
			String vcSAGA = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSAGA.getPosition());
			String vcSector = getStringValue(currentRow, Constants.CertificatesDatabasePos.COL_VCSECTOR.getPosition());
			
			data.setVcActivo(vcActivo);
			data.setVcAnexo(vcAnexo);
			data.setVcCertificado(vcCertificado);
			data.setVcCertificationGTD(vcCertificationGDT);
			data.setVcCode(vcCode);
			data.setVcComentarioAnexo(vcComentarioAnexo);
			data.setVcFechaCertificado(vcFechaCertificado == Constants.FUNDATIONDAYLESSONE ? null : vcFechaCertificado);
			data.setVcFechaExpiracion(vcFechaExpiracion == Constants.FUNDATIONDAYLESSONE ? null : vcFechaExpiracion);
			data.setVcIdCandidato(vcIdCandidato);
			data.setVcModulo(vcModulo);
			data.setVcNameGTD(vcNameGTD);
			data.setVcPartner(vcPartner);
			data.setVcSAGA(vcSAGA);
			data.setVcSector(vcSector);
			
			if(!data.getVcSAGA().equals(Constants.EMPTY)) {
				setCertificatesDataImportObject.add(data);
			}
			currentRow = sheet.getRow(i);
		}
		return setCertificatesDataImportObject;
	}
//	/**
//	 * Conver String date with format DD/MM/YYYY in Date Object 
//	 * @param date String date with format DD/MM/YYYY
//	 * @return Date Object with date input param
//	 */
//	private Date getDate(String date) {
//		if (date== null) {
//			return null;
//		}
//		String[] sDate = date.split("/");
//		return new Date (Integer.parseInt(sDate[2]), Integer.parseInt(sDate[1]), Integer.parseInt(sDate[0]));
//		
//	}
	/**
	 * Get Grade Value from row
	 * @param row		Excel row
	 * @param colum		Colum
	 * @return	String caracter
	 */
	private String getGradeValue(Row row, int colum) {
		return getStringValue(row, colum).substring(0,1);
	}

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
	private List<StaffingDataImport> saveAllStaffingDataImport(List<StaffingDataImport> staffingDataImportList, VersionStaffing verStaf) {
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
