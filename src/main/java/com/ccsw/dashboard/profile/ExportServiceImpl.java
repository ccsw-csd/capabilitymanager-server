package com.ccsw.dashboard.profile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.common.Report;
import com.ccsw.dashboard.config.literal.LiteralService;
import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileGroup;
import com.ccsw.dashboard.profile.model.ProfileTotal;
import com.ccsw.dashboard.reportversion.ReportVersionService;
import com.ccsw.dashboard.reportversion.model.ReportVersion;
import com.ccsw.dashboard.reportversion.model.ReportVersionDto;
import com.ccsw.dashboard.roleversion.RoleVersionService;
import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;
import com.ccsw.dashboard.staffingversion.StaffingVersionService;
import com.ccsw.dashboard.staffingversion.model.StaffingVersion;
import com.ccsw.dashboard.staffingversion.model.StaffingVersionDto;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExportServiceImpl implements ExportService {

	@Autowired
	private LiteralService literalService;

	private ReportVersion reportversion;

	@Autowired
	private ReportVersionService reportversionservice;

	@Autowired
	private StaffingVersionService staffingVersionService;

	@Autowired
	private RoleVersionService roleVersionService;

	List<ProfileTotal> profileTotals;
	List<ProfileGroup> profileGroup;

	public ExportServiceImpl(List<ProfileTotal> profileTotals, List<ProfileGroup> profileGroup) {
		this.profileTotals = profileTotals;
		this.profileGroup = profileGroup;
	}

	public List<ProfileTotal> getProfileTotals() {
		return profileTotals;
	}

	@Override
	public void setProfileTotals(List<ProfileTotal> profileTotals) {
		this.profileTotals = profileTotals;
	}

	public List<ProfileGroup> getProfileGroup() {
		return profileGroup;
	}

	@Override
	public void setProfileGroup(List<ProfileGroup> profileGroup) {
		this.profileGroup = profileGroup;
	}

	@Override
	public void writeProfileTotalsToCsv(String id, HttpServletResponse servletResponse) {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		servletResponse.setContentType("text/csv");

		servletResponse.addHeader("Content-Disposition", "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + ".csv");

		servletResponse.addHeader("Content-Disposition",
				"attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + ".csv");

		try (CSVPrinter csvPrinter = new CSVPrinter(servletResponse.getWriter(), CSVFormat.DEFAULT)) {
			csvPrinter.printRecord(id, "Total");
			for (ProfileTotal profileTotal : profileTotals) {
				csvPrinter.printRecord(profileTotal.getProfile(), profileTotal.getTotals().get(0));
			}
		} catch (IOException e) {

			// log.error("Error While writing CSV ", e);

		}

	}

	@Override
	public void writeProfileTotalsToExcel(String id, HttpServletResponse servletResponse) throws IOException {

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet(id);
		sheet.setColumnWidth(0, 10000);
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

		int i = 1;
		for (ProfileTotal profileTotal : profileTotals) {
			Row row = sheet.createRow(i++);
			Cell cell = row.createCell(0);
			cell.setCellValue(profileTotal.getProfile());
			cell.setCellStyle(style);
			cell = row.createCell(1);

			cell.setCellValue(profileTotal.getTotals().get(0));

			cell.setCellValue(profileTotal.getTotals().get(0));

			cell.setCellStyle(style);
		}

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		ServletOutputStream outputStream = servletResponse.getOutputStream();
		servletResponse.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + ".xlsx";
		servletResponse.setHeader(headerKey, headerValue);

		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public ReportVersionDto findVersion(Long idReport) {
		ReportVersion rv = this.reportversionservice.findById(idReport);
		ReportVersionDto rvdto = new ReportVersionDto();
		RoleVersion roleVersion = roleVersionService.findById(Long.valueOf(rv.getIdVersionCapacidades()));
		rvdto.setRoleVersion(roleVersion == null ? null
				: new RoleVersionDto(roleVersion.getId(), roleVersion.getIdTipoInterfaz(), roleVersion.getFechaImportacion(), roleVersion.getNumRegistros(), roleVersion.getNombreFichero(),
						roleVersion.getDescripcion(), roleVersion.getUsuario()));
		StaffingVersion staffingVersion = staffingVersionService.findById(Long.valueOf(rv.getIdVersionStaffing()));
		rvdto.setStaffingVersion(staffingVersion == null ? null
				: new StaffingVersionDto(staffingVersion.getId(), staffingVersion.getIdTipoInterfaz(), staffingVersion.getFechaImportacion(), staffingVersion.getNumRegistros(),
						staffingVersion.getNombreFichero(), staffingVersion.getDescripcion(), staffingVersion.getUsuario()));
		rvdto.setId(rv.getId());
		rvdto.setUsuario(rv.getUsuario());
		rvdto.setDescripcion(rv.getDescripcion());
		rvdto.setScreenshot(rv.getScreenshot());
		rvdto.setComentarios(rv.getComentarios());
		rvdto.setFechaImportacion(rv.getFechaImportacion());
		rvdto.setFechaModificacion(rv.getFechaModificacion());
		return rvdto;
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
	private int writeParametro(Sheet sheet, int rowNum, String key, Object value, CellStyle keyStyle, CellStyle valueStyle) {
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
	private CellStyle createCellStyle(Workbook workbook, boolean bold, IndexedColors color) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFillForegroundColor(color.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
	private CellStyle createDateCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		CreationHelper createHelper = workbook.getCreationHelper();
		style.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
		style.setAlignment(HorizontalAlignment.LEFT);
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
	private CellStyle createTitleStyle(Workbook workbook, boolean bold, IndexedColors color) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(color.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(bold);
		style.setFont(font);
		return style;
	}

	/**
	 *
	 * Método que escribe en un excel información los perfiles
	 *
	 * @param id              The ID used for naming the Excel file.
	 * @param servletResponse The HttpServletResponse object for sending the response.
	 * @param idReport        The ID of the report version.
	 * @throws IOException if an I/O error occurs while writing the Excel file or sending the response.
	 */
	@Override
	public void writeProfileToExcel(String id, HttpServletResponse servletResponse, Long idReport) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet parametros = workbook.createSheet(Constants.PARAMETROS);

		// Establecer anchos de columna
		parametros.setColumnWidth(0, 5000);
		parametros.setColumnWidth(1, 20000);

		// Establecer estilos de celda
		CellStyle keyParamStyle = createCellStyle(workbook, true, IndexedColors.GREY_25_PERCENT);
		CellStyle valueStyle = createCellStyle(workbook, false, IndexedColors.WHITE);
		CellStyle dateStyle = createDateCellStyle(workbook);
		CellStyle TitleStyle = createTitleStyle(workbook, true, IndexedColors.AQUA);

		// Crear fila para el título
		Row titleRow = parametros.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(Constants.PARAMETROS.toUpperCase());
		parametros.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		titleCell.setCellStyle(TitleStyle);

		ReportVersionDto reportVersion = findVersion(idReport);
		int rowNum = 1;

		// Información de parámetros
		rowNum = writeParametro(parametros, rowNum, Constants.VERSION, String.valueOf(reportVersion.getId()), keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.ROLES_FILE, reportVersion.getRoleVersion().getNombreFichero(), keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.STAFFING_FILE, reportVersion.getStaffingVersion().getNombreFichero(), keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.SCREENSHOOT, (reportVersion.getScreenshot() == 0) ? Constants.YES : Constants.NO, keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.DATE, Date.from(reportVersion.getFechaImportacion().atZone(ZoneId.systemDefault()).toInstant()), keyParamStyle, dateStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.DESCRIPTION, reportVersion.getDescripcion(), keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.USER, reportVersion.getUsuario(), keyParamStyle, valueStyle);
		rowNum = writeParametro(parametros, rowNum, Constants.COMENTS, reportVersion.getComentarios(), keyParamStyle, valueStyle);

		Sheet sheet = workbook.createSheet(id);
		int j = 0;

		sheet.setColumnWidth(j++, 2500);
		sheet.setColumnWidth(j++, 2500);
		sheet.setColumnWidth(j++, 3500);
		sheet.setColumnWidth(j++, 2500);
		sheet.setColumnWidth(j++, 3000);
		sheet.setColumnWidth(j++, 5000);
		sheet.setColumnWidth(j++, 10000);
		sheet.setColumnWidth(j++, 10000);
		sheet.setColumnWidth(j++, 10000);
		sheet.setColumnWidth(j++, 3500);
		sheet.setColumnWidth(j++, 3500);
		sheet.setColumnWidth(j++, 10000);
		sheet.setColumnWidth(j++, 15000);
		sheet.setColumnWidth(j++, 30000);
		sheet.setColumnWidth(j++, 15000);
		sheet.setColumnWidth(j++, 15000);
		sheet.setColumnWidth(j++, 15000);
		sheet.setColumnWidth(j++, 15000);
		sheet.setColumnWidth(j++, 15000);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		headerStyle.setFont(font);

		Row header = sheet.createRow(0);
		List<Literal> findByTypeAndSubtype = literalService.findBySubtype("d");
		j = 0;
		for (Literal literal : findByTypeAndSubtype) {
			Cell headerCell = header.createCell(j++);
			headerCell.setCellValue(literal.getDesc());
			headerCell.setCellStyle(headerStyle);
		}

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		int i = 1;
		for (ProfileGroup pgroup : profileGroup) {
			List<Profile> profileList = pgroup.getProfile();
			for (Profile profile : profileList) {

				j = 0;
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(j++);
				cell.setCellValue(profile.getGgid());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getSaga());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getPractica());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getGrado());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getCategoria());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getCentro());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getNombre());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getEmail());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getLocalizacion());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getStatus());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getPerfilStaffing());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getActual());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getPerfil());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getExperiencia());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				String perfilTecnico = (!profile.getTecnicoSolution().isEmpty()) ? profile.getTecnicoSolution() : "";
				perfilTecnico = (!profile.getTecnicoIntegration().isEmpty()) ? perfilTecnico.concat(";").concat(profile.getTecnicoIntegration()) : perfilTecnico;
				cell.setCellValue(perfilTecnico);
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getSkillCloudNative());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getSkillCloudNativeExperiencia());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getSkillLowCode());
				cell.setCellStyle(style);
				cell = row.createCell(j++);
				cell.setCellValue(profile.getSectorExperiencia());
				cell.setCellStyle(style);
			}
		}

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		ServletOutputStream outputStream = servletResponse.getOutputStream();
		servletResponse.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + "_Detail.xlsx";
		servletResponse.setHeader(headerKey, headerValue);

		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	@Override
	public void writeProfileToTemplateExcel(String id, HttpServletResponse servletResponse) throws IOException {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		ServletOutputStream outputStream = servletResponse.getOutputStream();
		servletResponse.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + "_Detail.xls";
		servletResponse.setHeader(headerKey, headerValue);

		Map<String, Object> data = new HashMap<>();
		List<Profile> profileList = new ArrayList<Profile>();

		for (ProfileGroup pgroup : profileGroup) {
			profileList.addAll(pgroup.getProfile());
		}
		data.put("profileList", profileList);
		data.put("createdAt", currentDateTime.substring(0, 10));
		Report report = new Report();
		report.createDocument(outputStream, "profileList", data);
	}

	public ReportVersion getreportversion() {
		return reportversion;
	}

	public void setreportversion(ReportVersion reportversion) {
		this.reportversion = reportversion;
	}
}
