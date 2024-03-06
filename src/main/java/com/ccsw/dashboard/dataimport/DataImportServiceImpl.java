package com.ccsw.dashboard.dataimport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.ccsw.dashboard.versionstaffing.VersionStaffingRepository;
import com.ccsw.dashboard.versionstaffing.model.VersionStaffing;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DataImportServiceImpl implements DataImportService {
	private static final Logger logger = LoggerFactory.getLogger(DataImportServiceImpl.class);

	private static final String EMPTY = "";
	private static final String XLS_FILE_FORMAT = "application/vnd.ms-excel";
	private static final String XLSX_FILE_FORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final List<String> ALLOWED_FORMATS = Arrays.asList(XLS_FILE_FORMAT, XLSX_FILE_FORMAT);

	private static final int FIRST_SHEET = 0;
	private static final int ROW_EVIDENCE_LIST_START = 1;
	private static final int ROW_EVIDENCE_LIST_NEXT = ROW_EVIDENCE_LIST_START + 1;

	private static final String ERROR_INIT  = ">>> [ERROR][DataImportServiceImpl] (";
	private static final String ERROR_INIT2  = ") ERROR: ";
	private static final String ERROR_EMPTY_ROL_FILE = " Rols List File is null or empty";
	private static final String ERROR_EMPTY_STAFFING_FILE = " Staffing List File is null or empty";
	private static final String ERROR_DOCUMENT_TYPE = "ERROR: 'documentType' param is not valid (select value 1, 2 or 3)";

	private static enum RolsDatabasePos { 
		COL_VCPROFILEEMAIL(3), 
		COL_VCPROFILENAME(4),
		COL_VCPROFILESAGA(5), 
//TODO:    	COL_VCPROFILEROLL1(0), inserta datos con unos scripts
		COL_VCPROFILEROLL1EXTENDIDO(6), 
		COL_VCPROFILEROLL2EM(7),
		COL_VCPROFILEROLL2AR(8), 
		COL_VCPROFILEROLL2AN(9),
		COL_VCPROFILEROLL2SE(10),
        COL_VCPROFILEROLEXPERIENCEEM(11),
        COL_VCPROFILEROLEXPERIENCEAR(12),
		COL_VCPROFILEROLL3(13), 
		COL_VCPROFILESKILLCLOUDNATIVEEXPERIENCE(14),
		COL_VCPROFILESKILLLOWCODEEXPERIENCE(15),
		COL_VCPROFILEROLL4(16), 
		COL_VCPROFILESECTOREXPERIENCE(17), 
		COL_VCPROFILESKILLCLOUDEXP(18);

		private final int excelPosition;
		RolsDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}
	
	private static enum StaffingDatabasePos {
		COL_VCPROFILESAGA(0),
		COL_VCPROFILEGGID(1),
		COL_VCPROFILENOMBRE(2),
		COL_VCPROFILEAPELLIDOS(3),
		COL_VCPROFILECATEGORIA(4),
		COL_VCPROFILEPRACTICA(5),
		COL_VCPROFILEGRADO(6),
		COL_VCPROFILECENTRO(7),
		COL_VCPROFILELOCALIZACION(8),
		COL_VCPROFILEPERFILTECNICO(9),
		COL_VCPROFILESTATUS(15);

		private final int excelPosition;

		StaffingDatabasePos(int position) {
			this.excelPosition = position;
		}

		public int getPosition() {
			return excelPosition;
		}
	}
//TODO: Add Enum CertificatesDatabasePos   

	@Autowired
	private FormDataImportRepository formDataImportRepository;

	@Autowired
	private StaffingDataImportRepository staffingDataImportRepository;
	
	@Autowired
	private VersionCapatidadesRepository versionCapatidadesRepository;
	
	@Autowired
	private VersionStaffingRepository versionStaffingRepository;
	
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
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(),importResponseDto, ERROR_DOCUMENT_TYPE, ERROR_DOCUMENT_TYPE, EMPTY, HttpStatus.BAD_REQUEST);
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
		Row currentRow = sheet.getRow(ROW_EVIDENCE_LIST_START);
		int sizeSheet = sheet.getPhysicalNumberOfRows();
		VersionCapacidades verCap = null;
		try {
			verCap = createCapacityVersion(sizeSheet,dto.getFileData().getOriginalFilename(), dto.getDescription(),dto.getUser(), 
					dto.getDocumentType(), dto.getFileData().getBytes());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		FormDataImport data = new FormDataImport();
		for (int i = ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new FormDataImport();
			//TODO: VCPROFILEROLL1 Se saca de unas formulas hacer las formulas
			String vcProfileSAGA = getStringValue (currentRow, RolsDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileEmail = getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEEMAIL.getPosition());
			String vcProfileName =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILENAME.getPosition());
			String vcProfileRoll1Extendido =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL1EXTENDIDO.getPosition());
			String vcProfileRoll2EM =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL2EM.getPosition());
			String vcProfileRoll2AR =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL2AR.getPosition());
			String vcProfileRoll2AN =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL2AN.getPosition());
			String vcProfileRoll2SE =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL2SE.getPosition());
			String vcProfileRolExperienceEM =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEEM.getPosition());
			String vcProfileRolExperienceAR =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLEXPERIENCEAR.getPosition());
			String vcProfileRoll3 =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL3.getPosition());
			String vcProfileSkillCloudNativeExperience =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILESKILLCLOUDNATIVEEXPERIENCE.getPosition());
			String vcProfileSkillLowCodeExperience =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILESKILLLOWCODEEXPERIENCE.getPosition());		
			String vcProfileRoll4 =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILEROLL4.getPosition());
			String vcProfileSectorExperience =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILESECTOREXPERIENCE.getPosition());
			String vcProfileSkillCloudExp =  getStringValue(currentRow, RolsDatabasePos.COL_VCPROFILESKILLCLOUDEXP.getPosition());

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
			
			formDataImportList.add(data);
			currentRow = sheet.getRow(i);
			data = new FormDataImport();
		}

		if (formDataImportList != null && !formDataImportList.isEmpty()) {
				saveAllFormDataImport(formDataImportList, verCap);
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2).append(ERROR_EMPTY_ROL_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(ERROR_EMPTY_ROL_FILE);
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
		Row currentRow = sheet.getRow(ROW_EVIDENCE_LIST_START);
		int sizeSheet = sheet.getPhysicalNumberOfRows();
		VersionStaffing verStaf = null;
		try {
			verStaf = createStaffingVersion(sizeSheet,dto.getFileData().getOriginalFilename(), dto.getDescription(), dto.getUser(), 
					dto.getDocumentType(), dto.getFileData().getBytes());
		} catch (Exception e) {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), importResponseDto, e, HttpStatus.UNPROCESSABLE_ENTITY );
			return importResponseDto;
		}
		
		StaffingDataImport data = new StaffingDataImport();
		for (int i = ROW_EVIDENCE_LIST_NEXT; currentRow != null; i++) {
			data = new StaffingDataImport();
			String vcProfileSAGA = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILESAGA.getPosition());
			String vcProfileGGID = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILEGGID.getPosition());
			String vcProfilePractica = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILEPRACTICA.getPosition());
			String vcProfileGrado = getGradeValue(currentRow, StaffingDatabasePos.COL_VCPROFILEGRADO.getPosition());
			String vcProfileCategoria = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILECATEGORIA.getPosition());
			String vcProfileCentro = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILECENTRO.getPosition());
			String vcProfileNombre = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILENOMBRE.getPosition());
			String vcProfileApellidos = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILEAPELLIDOS.getPosition());
			String vcProfileLocalizacion = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILELOCALIZACION.getPosition());
			String vcProfilePerfiltecnico = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILEPERFILTECNICO.getPosition());
			String vcProfileStatus = getStringValue (currentRow, StaffingDatabasePos.COL_VCPROFILESTATUS.getPosition());
			data.setVcProfileSAGA(vcProfileSAGA);
			data.setVcProfileGGID(vcProfileGGID);
			data.setVcProfilePractica(vcProfilePractica);
			data.setVcProfileGrado(vcProfileGrado);
			data.setVcProfileCategoria(vcProfileCategoria);
			data.setVcProfileCentro(vcProfileCentro);
			data.setVcProfileNombre(vcProfileNombre);
			data.setVcProfileApellidos(vcProfileApellidos);
			data.setVcProfileLocalizacion(vcProfileLocalizacion);
			data.setVcProfilePerfilTecnico(vcProfilePerfiltecnico);
			data.setVcProfileStatus(vcProfileStatus);

			data.setNumImportCodeID(verStaf.getId());
			
			staffingDataImportList.add(data);
			currentRow = sheet.getRow(i);
		}

		if (staffingDataImportList != null && !staffingDataImportList.isEmpty()) {
			saveAllStaffingDataImport(staffingDataImportList,verStaf);
			
		} else {
			StringBuilder errorData = new StringBuilder();
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2).append(ERROR_EMPTY_STAFFING_FILE);
			logger.error(errorData.toString());
			throw new UnprocessableEntityException(ERROR_EMPTY_STAFFING_FILE);
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
		// TODO: Recover Certificates Data
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2).append(" Funcion isnt developed");
		logger.error(errorData.toString());

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
			return workbook.getSheetAt(FIRST_SHEET);
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
		if (dto.getFileData().getOriginalFilename() == EMPTY) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2).append(" FileData is empty");
			logger.error(errorData.toString() );
			throw new UnsupportedMediaTypeException("FileData is empty");
		}
		if (!ALLOWED_FORMATS.contains(dto.getFileData().getContentType())) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2).append("FileData dont has valid format");
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
		String result = EMPTY;
		Cell col = row.getCell(column);
		if(col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = String.valueOf(col.getNumericCellValue());
			} else if (col.getCellType() == CellType.STRING){
				result = col.getStringCellValue();
			} else if(col.getCellType() == CellType.BOOLEAN) {
				result = String.valueOf(col.getBooleanCellValue());
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
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2);
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
			errorData.append(ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() ).append(ERROR_INIT2);
			logger.error(errorData.toString() + e.getMessage());
			throw new UnprocessableEntityException(errorData.toString());
		}
    }
	
	private  void setErrorToReturn( String function, ImportResponseDto importResponseDto, Exception e, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append( function ).append(ERROR_INIT2);
		
		setErrorToReturn(function, importResponseDto, e.getMessage(), e.getLocalizedMessage(), e.getStackTrace().toString(), status);
    }
	private  void setErrorToReturn( String function, ImportResponseDto importResponseDto, String errorMessage , String message, String trace, HttpStatus status) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append( function ).append(ERROR_INIT2);
		
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
}
