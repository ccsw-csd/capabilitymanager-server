package com.ccsw.dashboard.profile;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.profile.model.ProfileTotal;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExportServiceImpl {
	
	List<ProfileTotal> profileTotals;
	
	public ExportServiceImpl(List<ProfileTotal> profileTotals) {
		super();
		this.profileTotals = profileTotals;
	}	

	public void writeProfileTotalsToCsv(String id, HttpServletResponse servletResponse) {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename="+ id + "_" + currentDateTime.substring(0, 10) +".csv");
		
        try (CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord(id, "Total");
            for (ProfileTotal profileTotal : profileTotals) {
                csvPrinter.printRecord(profileTotal.getProfile(), profileTotal.getTotals().get(0));
            }
        } catch (IOException e) {
//			log.error("Error While writing CSV ", e);
        }
		
	}
	
	public void writeProfileTotalsToExcel(String id, HttpServletResponse servletResponse) throws IOException {
				
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet(id);
		sheet.setColumnWidth(0, 20000);
		sheet.setColumnWidth(1, 2000);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue(id);
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Total");
		headerCell.setCellStyle(headerStyle);
		
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		int i=1;
		for (ProfileTotal profileTotal : profileTotals) {
			Row row = sheet.createRow(i++);
			Cell cell = row.createCell(0);
			cell.setCellValue(profileTotal.getProfile());
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue((Long)profileTotal.getTotals().get(0));
			cell.setCellStyle(style);			
		}
				
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
//		File currDir = new File(".");
//		String path = currDir.getAbsolutePath();
//		String fileLocation = path.substring(0, path.length() - 1) + id + "_" + currentDateTime.substring(0, 10) + ".xlsx";
//		FileOutputStream outputStream = new FileOutputStream(fileLocation);

		ServletOutputStream outputStream = servletResponse.getOutputStream();		
		servletResponse.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + ".xlsx";
		servletResponse.setHeader(headerKey, headerValue);
		
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
