package com.ccsw.dashboard.profile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

		servletResponse.addHeader("Content-Disposition","attachment; filename="+ id + "_" + currentDateTime.substring(0, 10) +".csv");

		servletResponse.addHeader("Content-Disposition",
				"attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + ".csv");

			//			log.error("Error While writing CSV ", e);

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
<<<<<<< HEAD
			cell.setCellValue((Long) profileTotal.getTotals().get(0));
=======

			cell.setCellValue(profileTotal.getTotals().get(0));

			cell.setCellValue(profileTotal.getTotals().get(0));

>>>>>>> sprint-13
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

<<<<<<< HEAD
	public List<ReportVersionDto> findAll() {
		return this.reportversionservice.findAll().stream().map(rv -> {
			ReportVersionDto rvdto = new ReportVersionDto();
			RoleVersion roleVersion = roleVersionService.findById(Long.valueOf(rv.getIdVersionCapacidades()));
			rvdto.setRoleVersion(roleVersion == null ? null
					: new RoleVersionDto(roleVersion.getId(), roleVersion.getIdTipoInterfaz(),
							roleVersion.getFechaImportacion(), roleVersion.getNumRegistros(),
							roleVersion.getNombreFichero(), roleVersion.getDescripcion(), roleVersion.getUsuario()));
			StaffingVersion staffingVersion = staffingVersionService.findById(Long.valueOf(rv.getIdVersionStaffing()));
			rvdto.setStaffingVersion(staffingVersion == null ? null
					: new StaffingVersionDto(staffingVersion.getId(), staffingVersion.getIdTipoInterfaz(),
							staffingVersion.getFechaImportacion(), staffingVersion.getNumRegistros(),
							staffingVersion.getNombreFichero(), staffingVersion.getDescripcion(),
							staffingVersion.getUsuario()));
			rvdto.setId(rv.getId());
			rvdto.setUsuario(rv.getUsuario());
			rvdto.setDescripcion(rv.getDescripcion());
			rvdto.setScreenshot(rv.getScreenshot());
			rvdto.setComentarios(rv.getComentarios());
			rvdto.setFechaImportacion(rv.getFechaImportacion());
			rvdto.setFechaModificacion(rv.getFechaModificacion());
			return rvdto;
		}).toList();
	}
	


	@Override
	public void writeProfileToExcel(String id, HttpServletResponse servletResponse) throws IOException {

		Workbook workbook = new XSSFWorkbook();
=======

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




	@Override
	public void writeProfileToExcel(String id, HttpServletResponse servletResponse, Long idReport) throws IOException {

		Workbook workbook = new XSSFWorkbook();

>>>>>>> sprint-13
		Sheet parametros = workbook.createSheet("Parametros");

		int param = 0;
		parametros.setColumnWidth(param++, 5000);
		parametros.setColumnWidth(param++, 20000);

		// Crear una fila para el título
		Row titleRow = parametros.createRow(0);
		parametros.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

<<<<<<< HEAD
		// Crear una celda para el título
		CellStyle titleParam = workbook.createCellStyle();
		Cell titleCell = titleRow.createCell(0);
		CellStyle keyParam = workbook.createCellStyle();
		
=======
>>>>>>> sprint-13
		XSSFFont fonts = ((XSSFWorkbook) workbook).createFont();
		fonts.setFontName("Arial");
		fonts.setFontHeightInPoints((short) 10);
		fonts.setBold(true);
<<<<<<< HEAD
		titleParam.setFont(fonts);
		
		titleCell.setCellValue("PARAMETROS");
		titleParam.setAlignment(HorizontalAlignment.CENTER);
		titleParam.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		titleParam.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleCell.setCellStyle(titleParam);
		
=======

		// Crear una celda para el título
		CellStyle titleParamStyle = workbook.createCellStyle();
		titleParamStyle.setFont(fonts);
		titleParamStyle.setAlignment(HorizontalAlignment.CENTER);
		titleParamStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		titleParamStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle valueStyle = workbook.createCellStyle();
		valueStyle.setAlignment(HorizontalAlignment.LEFT);
		valueStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		valueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle keyParam = workbook.createCellStyle();
>>>>>>> sprint-13
		keyParam.setAlignment(HorizontalAlignment.LEFT);
		keyParam.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		keyParam.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		keyParam.setFont(fonts);
<<<<<<< HEAD
	
		CellStyle valueStyle = workbook.createCellStyle();
		valueStyle.setAlignment(HorizontalAlignment.LEFT);
		valueStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		valueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
=======

		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("PARAMETROS");
		titleCell.setCellStyle(titleParamStyle);
>>>>>>> sprint-13

		//formato de la fecha
		CellStyle styleParam = workbook.createCellStyle();
		styleParam.setWrapText(true);
		CreationHelper createHelper = workbook.getCreationHelper();
		styleParam.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
		styleParam.setAlignment(HorizontalAlignment.LEFT);
<<<<<<< HEAD
		styleParam.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		styleParam.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		List<ReportVersionDto> reportVersions = findAll();
		int rowNum = 1;

		for (ReportVersionDto paramRole : reportVersions) {
			Row row = parametros.createRow(rowNum++);
			LocalDateTime fechaImportacion = paramRole.getFechaImportacion();
			
			Cell cellHeader = row.createCell(0);
			Cell cellValue = row.createCell(1);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Versión");
			cellValue.setCellValue(paramRole.getId());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Archivo Roles");
			cellValue.setCellValue(paramRole.getRoleVersion().getNombreFichero());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Archivo Staffing");
			cellValue.setCellValue(paramRole.getStaffingVersion().getNombreFichero());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("ScreenShot");
			cellValue.setCellValue(paramRole.getScreenshot());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Fecha de Generación");
			cellValue.setCellValue(Date.from(fechaImportacion.atZone(ZoneId.systemDefault()).toInstant()));
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(styleParam);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Descripción");
			cellValue.setCellValue(paramRole.getDescripcion());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Usuario");
			cellValue.setCellValue(paramRole.getUsuario());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			row = parametros.createRow(rowNum++);
			cellHeader = row.createCell(0);
			cellValue = row.createCell(1);
			cellHeader.setCellValue("Comentarios");
			cellValue.setCellValue(paramRole.getComentarios());
			cellHeader.setCellStyle(keyParam);
			cellValue.setCellStyle(valueStyle);

			break;

		}

		Sheet sheet = workbook.createSheet(id);

		int j = 0;
=======
		styleParam.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		styleParam.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		ReportVersionDto reportVersion = findVersion(idReport);
		int rowNum = 1;

		Row row = parametros.createRow(rowNum++);
		LocalDateTime fechaImportacion = reportVersion.getFechaImportacion();

		Cell cellHeader = row.createCell(0);
		Cell cellValue = row.createCell(1);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Versión");
		cellValue.setCellValue(reportVersion.getId());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Archivo Roles");
		cellValue.setCellValue(reportVersion.getRoleVersion().getNombreFichero());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Archivo Staffing");
		cellValue.setCellValue(reportVersion.getStaffingVersion().getNombreFichero());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("ScreenShot");
		cellValue.setCellValue((reportVersion.getScreenshot()==0)?"NO":"SI");
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Fecha de Generación");
		cellValue.setCellValue(Date.from(fechaImportacion.atZone(ZoneId.systemDefault()).toInstant()));
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(styleParam);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Descripción");
		cellValue.setCellValue(reportVersion.getDescripcion());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Usuario");
		cellValue.setCellValue(reportVersion.getUsuario());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);

		row = parametros.createRow(rowNum++);
		cellHeader = row.createCell(0);
		cellValue = row.createCell(1);
		cellHeader.setCellValue("Comentarios");
		cellValue.setCellValue(reportVersion.getComentarios());
		cellHeader.setCellStyle(keyParam);
		cellValue.setCellStyle(valueStyle);


		Sheet sheet = workbook.createSheet(id);
		int j = 0;

>>>>>>> sprint-13
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
<<<<<<< HEAD
				j = 0;
				Row row = sheet.createRow(i++);
=======

				j = 0;
				row = sheet.createRow(i++);
>>>>>>> sprint-13
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
				String perfilTecnico = (!profile.getTecnicoSolution().isEmpty())?profile.getTecnicoSolution():"";
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

<<<<<<< HEAD
=======

>>>>>>> sprint-13
		ServletOutputStream outputStream = servletResponse.getOutputStream();
		servletResponse.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + id + "_" + currentDateTime.substring(0, 10) + "_Detail.xlsx";
		servletResponse.setHeader(headerKey, headerValue);

		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
<<<<<<< HEAD
	
	
=======
>>>>>>> sprint-13

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
