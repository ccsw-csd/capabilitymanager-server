package com.ccsw.capabilitymanager.utils;

import com.ccsw.capabilitymanager.common.exception.BadRequestException;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

/**
 * @author pajimene
 *
 */
public interface UtilsService {

  /**
   * @return
   */
  String getVersion();

  public Sheet obtainSheet(MultipartFile file) throws BadRequestException;

  public Sheet obtainSheetFromInputStream(InputStream inputStream) throws BadRequestException;

  public void checkInputObject(ImportRequestDto dto);

  public String getStringValue(Row row, int column);

  public Integer getIntValue(Row row, int column);

  public double getDecimalValue(Row row, int column);

  public String getGradeValue(Row row, int colum);

  public Date getDateValue(Row row, int column);

  public int writeParametro(Sheet sheet, int rowNum, String key, Object value, CellStyle keyStyle, CellStyle valueStyle);

  public CellStyle createCellStyle(Workbook workbook, boolean bold, IndexedColors color);

  public CellStyle createDateCellStyle(Workbook workbook);

  public CellStyle createTitleStyle(Workbook workbook, boolean bold, IndexedColors color);

}
