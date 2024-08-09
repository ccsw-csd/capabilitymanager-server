package com.ccsw.capabilitymanager.utils;

import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.BadRequestException;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.exception.UnsupportedMediaTypeException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;

/**
 * @author ccsw
 *
 */
@Service
public class UtilsServiceImpl implements UtilsService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion() {

		try {
			return new Manifest(UtilsServiceImpl.class.getResourceAsStream("/META-INF/MANIFEST.MF")).getMainAttributes()
					.get(Attributes.Name.IMPLEMENTATION_VERSION).toString();
		} catch (Exception e) {
			CapabilityLogger.logError(Constants.ERROR_INIT_UTIL + "getVersion) : Error al extraer la versión.");
		}

		return "?";
	}

	/**
	 * Get the main Excel tab given a file
	 * 
	 * @param file Excel File
	 * @return selected Excel tab
	 * @throws BadRequestException It is not possible to read the provided file
	 */
	public Sheet obtainSheet(MultipartFile file) throws BadRequestException {
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			return workbook.getSheetAt(Constants.FIRST_SHEET);
		} catch (Exception e) {
			CapabilityLogger.logError(Constants.ERROR_INIT_UTIL + "obtainSheet) : Ha ocurrido un error leyendo el fichero. Comprueba la validación de los datos y si no estan encriptados.");
			throw new BadRequestException(
					"An error occurred reading the file. Check the validity of the data and that it is not encrypted.");
		}
	}

	/**
	 * Check Input Object if ContentType is not valid or fileData is empty or null
	 * get throw
	 * 
	 * @param dto ImportRequestDto Object
	 * @throws UnsupportedMediaTypeException or UnprocessableEntityException
	 */
	public void checkInputObject(ImportRequestDto dto) {
		CapabilityLogger.logDebug(" >>>> checkInputObject ");
		if (dto.getFileData().getOriginalFilename() == Constants.EMPTY) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2).append(" FileData is empty");
			CapabilityLogger.logError(Constants.ERROR_INIT_UTIL + "checkInputObject) : El archivo de datos está vacío.");
			throw new UnsupportedMediaTypeException("FileData is empty");
		}
		if (!Constants.ALLOWED_FORMATS.contains(dto.getFileData().getContentType())) {
			StringBuilder errorData = new StringBuilder();
			errorData.append(Constants.ERROR_INIT).append(Thread.currentThread().getStackTrace()[1].getMethodName())
					.append(Constants.ERROR_INIT2).append("FileData dont has valid format");
			CapabilityLogger.logError(Constants.ERROR_INIT_UTIL + "checkInputObject) : El archivo de datos no tiene un formato válido.");
			throw new UnprocessableEntityException("FileData dont has valid format");
		}
		CapabilityLogger.logDebug("      checkInputObject >>>>");
	}

	/**
	 * Get String value from Row
	 * 
	 * @param row    to recover value
	 * @param column value to recover
	 * @return column value in string format
	 */
	public String getStringValue(Row row, int column) {
		String result = Constants.EMPTY;
		Cell col = row.getCell(column);
		if (col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = String.valueOf((int) col.getNumericCellValue());
			} else if (col.getCellType() == CellType.STRING) {
				result = col.getStringCellValue();
			} else if (col.getCellType() == CellType.BOOLEAN) {
				result = String.valueOf(col.getBooleanCellValue());
			}
		}
		return result;
	}
	/**
	 * Get String value from Row
	 * 
	 * @param row    to recover value
	 * @param column value to recover
	 * @return column value in string format
	 */
	public Integer getIntValue(Row row, int column) {
		String result = "0";
		Cell col = row.getCell(column);
		if (col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = String.valueOf((int) col.getNumericCellValue());
			} else if (col.getCellType() == CellType.STRING) {
				result = col.getStringCellValue();
			} else if (col.getCellType() == CellType.BOOLEAN) {
				result = String.valueOf(col.getBooleanCellValue());
			}
		}
		return Integer.valueOf(result);
	}
	
	/**
	 * Get Decimal value from Row
	 * 
	 * @param row    to recover value
	 * @param column value to recover
	 * @return column value as double
	 */
	public double getDecimalValue(Row row, int column) {
	    double result = 0.0; // Valor predeterminado en caso de que la celda esté vacía
	    Cell col = row.getCell(column);
	    if (col != null) {
	        if (col.getCellType() == CellType.NUMERIC) {
	            result = col.getNumericCellValue();
	        } else if (col.getCellType() == CellType.STRING) {
	            try {
	                result = Double.parseDouble(col.getStringCellValue());
	            } catch (NumberFormatException e) {
	                // Manejar errores de conversión
					CapabilityLogger.logError(Constants.ERROR_INIT_UTIL + "getDecialValue): Error al convertir la cadena de string a double.");
	                System.err.println("Error al convertir la cadena a double: " + e.getMessage());
	            }
	        } else if (col.getCellType() == CellType.BOOLEAN) {
	            result = col.getBooleanCellValue() ? 1.0 : 0.0;
	        }
	    }
	    return result;
	}
	
	/**
	 * Get Grade Value from row
	 * 
	 * @param row   Excel row
	 * @param colum Colum
	 * @return String caracter
	 */
	public String getGradeValue(Row row, int colum) {
		return getStringValue(row, colum).substring(0, 1);
	}

	/**
	 * Get Date value from Row
	 * 
	 * @param row    to recover date value
	 * @param column value to recover
	 * @return column value in Date format
	 */
	public Date getDateValue(Row row, int column) {
		Date result = null; // Constants.FUNDATIONDAYLESSONE;
		Cell col = row.getCell(column);
		if (col != null) {
			if (col.getCellType() == CellType.NUMERIC) {
				result = col.getDateCellValue();
			}
		}
		return result;
	}

	/**
	 * Método que escribe los datos en cada campo
	 *
	 * @param sheet
	 * @param rowNum
	 * @param key
	 * @param value
	 * @param keyStyle
	 * @param valueStyle
	 * @return
	 */
	public int writeParametro(Sheet sheet, int rowNum, String key, Object value, CellStyle keyStyle,
			CellStyle valueStyle) {
		Row row = sheet.createRow(rowNum++);
		Cell cellHeader = row.createCell(0);
		cellHeader.setCellValue(key);
		cellHeader.setCellStyle(keyStyle);
		Cell cellValue = row.createCell(1);
		if (value instanceof Date) {
			cellValue.setCellValue((Date) value);
		} else {
			cellValue.setCellValue(value.toString());
		}
		cellValue.setCellStyle(valueStyle);
		return rowNum;
	}

	/**
	 * Método que le da estilo a las celdas
	 *
	 * @param workbook
	 * @param bold
	 * @param color
	 * @return
	 */
	public CellStyle createCellStyle(Workbook workbook, boolean bold, IndexedColors color) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFillForegroundColor(color.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(bold);
		style.setFont(font);
		return style;
	}

	/**
	 * Estilo de los datos en cada celda
	 *
	 * @param workbook
	 * @return
	 */
	public CellStyle createDateCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		style.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderRight(BorderStyle.THIN);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	/**
	 * estilo del titulo
	 *
	 * @param workbook
	 * @param bold
	 * @param color
	 * @return
	 */
	public CellStyle createTitleStyle(Workbook workbook, boolean bold, IndexedColors color) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(color.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(bold);
		style.setFont(font);
		return style;
	}

}
