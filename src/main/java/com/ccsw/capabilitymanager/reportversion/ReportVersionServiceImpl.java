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

	
	/**
	 * Retrieves all {@link ReportVersion} entities from the repository.
	 * 
	 * <p>This method fetches all `ReportVersion` records from the underlying data store, sorts them, and returns the sorted list.</p>
	 *
	 * @return A sorted list of all {@link ReportVersion} entities available in the repository.
	 */
	@Override
	public List<ReportVersion> findAll() {
		return this.reportVersionRepository.findAll().stream().sorted().toList();
	}

	/**
	 * Retrieves a {@link ReportVersion} entity by its version capabilities ID.
	 * 
	 * <p>This method queries the repository to find a {@link ReportVersion} entity that matches the given ID for version capabilities. 
	 * If no entity is found, it returns {@code null}.</p>
	 *
	 * @param id The ID of the version capabilities to search for.
	 * @return The {@link ReportVersion} entity with the specified ID, or {@code null} if no entity with that ID exists.
	 */
	@Override
	public ReportVersion findByIdVersionCapacidades(Long id) {
		return this.reportVersionRepository.findByIdVersionCapacidades(id).orElse(null);
	}

	
	/**
	 * Retrieves a list of {@link ReportVersion} entities based on the screenshot ID and optional year filter.
	 * 
	 * <p>If a year is provided, the method fetches all {@link ReportVersion} entities where the screenshot ID matches and
	 * the import date falls within the specified year. If the screenshot ID is 0 or 1, it is used to filter results, otherwise, 
	 * only the date range is used for filtering. If the year is not provided, the method only filters by screenshot ID.</p>
	 * 
	 * @param id The screenshot ID to filter the {@link ReportVersion} entities. If this ID is 0 or 1, it will be used in the query.
	 *           If the ID is not 0 or 1, or if it is not a valid number, only the date range will be used for filtering.
	 * @param year The year to filter {@link ReportVersion} entities by their import date. If {@code null}, only the screenshot ID is used.
	 * @return A list of {@link ReportVersion} entities that match the provided screenshot ID and fall within the specified date range.
	 */
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

	/**
	 * Retrieves a list of distinct years from {@link ReportVersion} entities based on an optional screenshot filter.
	 * 
	 * <p>If a screenshot ID is provided, the method fetches all {@link ReportVersion} entities with that screenshot ID and extracts
	 * the distinct years from their import dates. If no screenshot ID is provided, it extracts distinct years from all {@link ReportVersion} entities.</p>
	 * 
	 * @param screenshot The screenshot ID to filter {@link ReportVersion} entities. If {@code null}, no filtering by screenshot ID is applied.
	 * @return A list of distinct years as strings, representing the years in which {@link ReportVersion} entities were imported.
	 */
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

	/**
	 * Saves or updates a {@link ReportVersion} entity based on the provided ID and DTO.
	 * 
	 * <p>If the ID is not zero and no existing {@link ReportVersion} entity is found, an exception is thrown. If the existing
	 * {@link ReportVersion} entity's screenshot value is different from the DTO's screenshot value, the entity's user and modification
	 * date are updated. Properties from the DTO are then copied to the entity, excluding certain fields.</p>
	 * 
	 * @param id The ID of the {@link ReportVersion} entity to be updated. If the ID is zero, a new entity is not created.
	 * @param dto The {@link ReportVersionDto} containing the data to update the entity.
	 * @throws MyBadAdviceException if the {@link ReportVersion} entity with the provided ID does not exist.
	 */
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

	/**
	 * Finds a {@link ReportVersion} entity by its ID.
	 * 
	 * @param id The ID of the {@link ReportVersion} entity to be retrieved.
	 * @return The {@link ReportVersion} entity with the specified ID, or {@code null} if no such entity exists.
	 */
	@Override
	public ReportVersion findById(Long id) {
		return reportVersionRepository.findById(id).orElse(null);
	}

	/**
	 * Generates and saves a new {@link ReportVersion} based on the provided DTO.
	 * 
	 * <p>This method handles the creation of associated {@link CertificatesRolesVersion} entities by calling 
	 * {@link #certificatesRoleSave(GenerateReportVersionDto)}. It then creates a new {@link ReportVersion} entity using the data 
	 * from the DTO and saves it to the repository.</p>
	 * 
	 * @param dto The {@link GenerateReportVersionDto} containing the data needed to generate and save the new {@link ReportVersion}.
	 * @return The saved {@link ReportVersion} entity.
	 */
	@Override
	public ReportVersion generateReport(GenerateReportVersionDto dto) {
		certificatesRoleSave(dto);
		ReportVersion reportVersion = getReportVersion(dto);
		return reportVersionRepository.save(reportVersion);
	}
	
	/**
	 * Saves {@link CertificatesRolesVersion} entities based on the provided DTO and related certificates data.
	 * 
	 * <p>This method retrieves certificates data from the repository and maps it to {@link CertificatesRolesVersion} entities
	 * according to the role information. It then saves the list of {@link CertificatesRolesVersion} entities to the repository.</p>
	 * 
	 * @param dto The {@link GenerateReportVersionDto} containing data used to filter and create {@link CertificatesRolesVersion} entities.
	 */
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
