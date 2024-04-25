package com.ccsw.dashboard.utils;

import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.common.exception.BadRequestException;
import com.ccsw.dashboard.common.exception.UnprocessableEntityException;
import com.ccsw.dashboard.common.exception.UnsupportedMediaTypeException;
import com.ccsw.dashboard.dataimport.model.ImportRequestDto;

/**
 * @author ccsw
 *
 */
@Service
public class UtilsServiceImpl implements UtilsService {

	private static final Logger LOG = LoggerFactory.getLogger(UtilsServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion() {

		try {
			return new Manifest(UtilsServiceImpl.class.getResourceAsStream("/META-INF/MANIFEST.MF")).getMainAttributes().get(Attributes.Name.IMPLEMENTATION_VERSION).toString();
		} catch (Exception e) {
			LOG.error("Error al extraer la version");
		}

		return "?";
	}

	/**
	 * Get the main Excel tab given a file
	 * @param file 	Excel File
	 * @return 		selected Excel tab
	 * @throws BadRequestException It is not possible to read the provided file
	 */
	public Sheet obtainSheet(MultipartFile file) throws BadRequestException {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			return workbook.getSheetAt(Constants.FIRST_SHEET);
		} catch (Exception e) {
			throw new BadRequestException("An error occurred reading the file. Check the validity of the data and that it is not encrypted.");
		}
	}

	/**
	 * Gets the tab of an Excel with name <code>nameSheet</code>
	 * @param file 	Excel File
	 * @return 		selected Excel tab
	 * @throws BadRequestException It is not possible to read the provided file
	 */
	public Sheet obtainSheet(MultipartFile file, String nameSheet) throws BadRequestException {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			return workbook.getSheet(nameSheet);
		} catch (Exception e) {
			throw new BadRequestException("An error occurred reading the file. Check the validity of the data and that it is not encrypted.");
		}
	}

	/**
	 * Check Input Object if ContentType is not valid or fileData is empty or null get throw
	 * @param 	dto ImportRequestDto Object
	 * @throws 	UnsupportedMediaTypeException or UnprocessableEntityException
	 */
	public void checkInputObject(ImportRequestDto dto) {
		LOG.debug(" >>>> checkInputObject ");
		if (dto.getFileData().getOriginalFilename() == Constants.EMPTY) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append(" FileData is empty");
			LOG.error(errorData.toString() );
			throw new UnsupportedMediaTypeException("FileData is empty");
		}
		if (!Constants.ALLOWED_FORMATS.contains(dto.getFileData().getContentType())) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append( Thread.currentThread().getStackTrace()[1].getMethodName() )
			.append(Constants.ERROR_INIT2).append("FileData dont has valid format");
			LOG.error(errorData.toString());
			throw new UnprocessableEntityException("FileData dont has valid format");
		}
		LOG.debug("      checkInputObject >>>>");
	}

	/**
	 * Get String value from Row
	 * @param row		to recover value
	 * @param column	value to recover
	 * @return 			column value in string format
	 */
	public String getStringValue(Row row, int column) {
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
	 * Get Grade Value from row
	 * @param row		Excel row
	 * @param colum		Colum
	 * @return	String caracter
	 */
	public String getGradeValue(Row row, int colum) {
		return getStringValue(row, colum).substring(0,1);
	}


	/**
	 * Get Date value from Row
	 * @param row		to recover date value
	 * @param column	value to recover
	 * @return 			column value in Date format
	 */
	public Date getDateValue(Row row, int column) {
		Date result = null; // Constants.FUNDATIONDAYLESSONE;
		Cell col = row.getCell(column);
		if(col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = col.getDateCellValue();
			}
		}
		return result;
	}
}
