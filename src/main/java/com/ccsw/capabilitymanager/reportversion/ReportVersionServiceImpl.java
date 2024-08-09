package com.ccsw.capabilitymanager.reportversion;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.reportversion.model.CertificatesRolesVersion;
import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportVersionServiceImpl implements ReportVersionService {
	private static final String ERROR_INIT = ">>> [ERROR][ReportVersionServiceImpl] (";
	@Autowired
	private ReportVersionRepository reportVersionRepository;
	
	@Autowired
	private CertificatesRolesVersionRepository certificatesRolesVersionRepository;
	
	@Autowired
	private CertificatesDataImportRepository certificatesDataImportRepository;
	
	@Autowired
	private FormDataImportRepository formDataImportRepository;

	@Override
	public List<ReportVersion> findAll() {
		return this.reportVersionRepository.findAll().stream().sorted().toList();
	}

	@Override
	public ReportVersion findByIdVersionCapacidades(Long id) {
		return this.reportVersionRepository.findByIdVersionCapacidades(id).orElse(null);
	}

	@Override
	public List<ReportVersion> findByScreenshot(String id, String year) {

		if (year != null) {
			String str1 = year + "-01-01 00:00";
			String str2 = year + "-12-31 23:59";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
			LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);

			try {
				int d = Integer.parseInt(id);
				if (d == 0 || d == 1) {
					return this.reportVersionRepository.findByScreenshotAndFechaImportacionBetween(d, dateTime1,
							dateTime2);
				} else {
					return this.reportVersionRepository.findByFechaImportacionBetween(dateTime1, dateTime2);
				}
			} catch (NumberFormatException nfe) {
				return this.reportVersionRepository.findByFechaImportacionBetween(dateTime1, dateTime2);
			}
		} else {
			int b = Integer.parseInt(id);
			return this.reportVersionRepository.findByScreenshot(b);
		}

	}

	@Override
	public List<String> findYears(String screenshot) {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<ReportVersion> listReportVersion = new ArrayList<ReportVersion>();
		if (screenshot != null) {
			listReportVersion = findByScreenshot(screenshot, null);
		} else {
			listReportVersion = findAll();
		}
		for (ReportVersion reportVersion : listReportVersion) {
			String year = String.valueOf(reportVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");
		}

		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());
		}

		return rvList;
	}

	@Override
	public void save(Long id, ReportVersionDto dto) {
		ReportVersion reportVersion;
		reportVersion = this.findById(id);
		if (reportVersion == null && id != 0) {
			CapabilityLogger.logError(ERROR_INIT + "save) : Error al guardar ReportVersion el id no existe.");
			throw new MyBadAdviceException("reportVersion id doesn't exist");
		}
		if (reportVersion.getScreenshot() != dto.getScreenshot()) {
			reportVersion.setUsuario(dto.getUsuario());
			LocalDate ld = LocalDate.now();
			LocalTime lt = LocalTime.now();
			reportVersion.setFechaModificacion(LocalDateTime.of(ld, lt));
		}
		BeanUtils.copyProperties(dto, reportVersion, "usuario", "fechaModificacion", "idVersionCapacidades",
				"idVersionStaffing", "idVersionCertificaciones", "fechaImportacion", "descripcion");
		// roleVersion.setFechaimportacion(dto.getFechaImportacion());
		this.reportVersionRepository.save(reportVersion);
	}

	@Override
	public ReportVersion findById(Long id) {
		return reportVersionRepository.findById(id).orElse(null);
	}

	@Override
	public ReportVersion generateReport(GenerateReportVersionDto dto) {
		certificatesRoleSave(dto);
		ReportVersion reportVersion = getReportVersion(dto);
		return reportVersionRepository.save(reportVersion);
	}
	
	
	public void certificatesRoleSave(GenerateReportVersionDto dto) {
		List<CertificatesDataImport> lista = certificatesDataImportRepository.findByNumImportCodeId((long) dto.getIdVersionCertificaciones());
		List<CertificatesRolesVersion> certificatesRoles = getCertificatesRoles(lista,dto);
		certificatesRolesVersionRepository.saveAll(certificatesRoles);
	}
	
	private List<CertificatesRolesVersion> getCertificatesRoles(List<CertificatesDataImport> dto, GenerateReportVersionDto version) {
	    List<CertificatesRolesVersion> certificatesRolesVersions = new ArrayList<>();
	    LocalDateTime ldt = LocalDateTime.now();

	    for (CertificatesDataImport dataImport : dto) {
	    	
	    	FormDataImport role = formDataImportRepository.findBySAGAAndNumImportCodeId(dataImport.getSAGA(),version.getIdRoleVersion());
	    	
	        CertificatesRolesVersion certificatesRolesVersion = new CertificatesRolesVersion();

	    	if(role.getRolL1().equals("Architects")) {

	        certificatesRolesVersion.setSAGA(dataImport.getSAGA());
	        certificatesRolesVersion.setFechaCarga(dataImport.getFechaCertificado());
	        certificatesRolesVersion.setRolFormulario(role.getRolL2AR());
	        certificatesRolesVersion.setCertificado(dataImport.getCertificado());
	        certificatesRolesVersion.setUsuario(version.getUser());
	        certificatesRolesVersion.setNum_import_code_id(version.getIdVersionCertificaciones());
	        
	        certificatesRolesVersions.add(certificatesRolesVersion);
	       }else if(role.getRolL1().equals("Engagement Managers")){
		    certificatesRolesVersion.setSAGA(dataImport.getSAGA());
		    certificatesRolesVersion.setFechaCarga(dataImport.getFechaCertificado());
		    certificatesRolesVersion.setRolFormulario(role.getRolL2EM());
		    certificatesRolesVersion.setCertificado(dataImport.getCertificado());
		    certificatesRolesVersion.setUsuario(version.getUser());   
		    certificatesRolesVersion.setNum_import_code_id(version.getIdVersionCertificaciones());
		    
		    certificatesRolesVersions.add(certificatesRolesVersion);
		    
	       }
	    }

	    return certificatesRolesVersions;
	}
	
	private ReportVersion getReportVersion(GenerateReportVersionDto dto) {
		LocalDateTime ldt = LocalDateTime.now();
		ReportVersion reportVersion = new ReportVersion();

		reportVersion.setScreenshot(0);

		reportVersion.setIdVersionCapacidades(dto.getIdRoleVersion());
		reportVersion.setIdVersionStaffing(dto.getIdStaffingVersion());
		reportVersion.setIdVersionCertificaciones(dto.getIdVersionCertificaciones());

		reportVersion.setDescripcion(dto.getDescription());
		reportVersion.setComentarios(dto.getComments());
		reportVersion.setUsuario(dto.getUser());
		reportVersion.setFechaImportacion(ldt);
		reportVersion.setFechaModificacion(ldt);
		return reportVersion;
	}

}
