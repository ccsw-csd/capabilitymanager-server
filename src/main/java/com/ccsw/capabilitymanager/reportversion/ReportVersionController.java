package com.ccsw.capabilitymanager.reportversion;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.ccsw.capabilitymanager.certificatesversion.CertificatesService;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;
import com.ccsw.capabilitymanager.roleversion.RoleVersionService;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;
import com.ccsw.capabilitymanager.staffingversion.StaffingVersionService;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

@RequestMapping(value = "/reportimports")
@RestController
public class ReportVersionController {

	@Autowired
	private ReportVersionService reportVersionService;

	@Autowired
	private StaffingVersionService staffingVersionService;
	
	@Autowired
	private CertificatesService certificatesService;

	@Autowired
	private RoleVersionService roleVersionService;

	@Autowired
	DozerBeanMapper mapper;

	/**
	 * Retrieves a list of {@link ReportVersionDto} objects representing all report versions.
	 * 
	 * <p>This method fetches all report versions from the service layer and maps each {@link ReportVersion}
	 * entity to a {@link ReportVersionDto}. For each report version, it also fetches related {@link RoleVersion},
	 * {@link StaffingVersion}, and {@link CertificatesVersion} entities and maps them to their corresponding DTOs.</p>
	 * 
	 * <p>The method performs the following steps for each {@link ReportVersion}:</p>
	 * <ul>
	 *   <li>Fetches the related {@link RoleVersion} entity by its ID and maps it to {@link RoleVersionDto}.
	 *       If no entity is found, the DTO is set to null.</li>
	 *   <li>Fetches the related {@link StaffingVersion} entity by its ID and maps it to {@link StaffingVersionDto}.
	 *       If no entity is found, the DTO is set to null.</li>
	 *   <li>Fetches the related {@link CertificatesVersion} entity by its ID and maps it to {@link CertificatesVersionDto}.
	 *       If no entity is found, the DTO is set to null.</li>
	 *   <li>Maps the {@link ReportVersion} fields to the corresponding fields in {@link ReportVersionDto}.</li>
	 * </ul>
	 * 
	 * @return A list of {@link ReportVersionDto} objects, each representing a report version with its associated
	 *         role version, staffing version, and certificates version details.
	 */
	@GetMapping("/all")
	public List<ReportVersionDto> findAll() {
		return this.reportVersionService.findAll().stream().map(rv -> {
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
			CertificatesVersion certificatesVersion = certificatesService.findById(Long.valueOf(rv.getIdVersionCertificaciones()));
			rvdto.setCertificatesVersion(certificatesVersion == null ? null
					: new CertificatesVersionDto(certificatesVersion.getId(), certificatesVersion.getIdTipoInterfaz(),
							certificatesVersion.getFechaImportacion(), certificatesVersion.getNumRegistros(),
							certificatesVersion.getNombreFichero(), certificatesVersion.getDescripcion(),
							certificatesVersion.getUsuario()));
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

	/**
	 * Retrieves a list of {@link ReportVersionDto} objects representing all report versions for a specified year.
	 * 
	 * <p>This method filters the report versions to include only those whose import date (`FechaImportacion`) matches
	 * the specified year. It then maps each {@link ReportVersion} entity to a {@link ReportVersionDto}.</p>
	 * 
	 * <p>The method performs the following steps:</p>
	 * <ul>
	 *   <li>Filters the list of report versions to include only those where the year of the import date (`FechaImportacion`)
	 *       matches the provided year.</li>
	 *   <li>Maps each {@link ReportVersion} to a {@link ReportVersionDto}, including related versions:</li>
	 *   <ul>
	 *     <li>Fetches and maps the related {@link RoleVersion} entity to {@link RoleVersionDto}. If no entity is found, 
	 *         the DTO is set to null.</li>
	 *     <li>Fetches and maps the related {@link StaffingVersion} entity to {@link StaffingVersionDto}. If no entity is found, 
	 *         the DTO is set to null.</li>
	 *     <li>Fetches and maps the related {@link CertificatesVersion} entity to {@link CertificatesVersionDto}. If no entity is found, 
	 *         the DTO is set to null.</li>
	 *   </ul>
	 *   <li>Maps the remaining fields of {@link ReportVersion} to {@link ReportVersionDto}.</li>
	 * </ul>
	 * 
	 * @param year The year to filter the report versions by, provided as a string. It should represent a valid year (e.g., "2023").
	 * 
	 * @return A list of {@link ReportVersionDto} objects for the specified year. Each DTO contains details about the report version,
	 *         along with related role version, staffing version, and certificates version information.
	 */
	@GetMapping("/all/{year}")
	public List<ReportVersionDto> findAllYear(@PathVariable String year) {
		return this.reportVersionService.findAll().stream()
				.filter(rv -> String.valueOf(rv.getFechaImportacion().getYear()).equals(year))
				// .map(rv->mapper.map(rv, ReportVersionDto.class))
				.map(rv -> {
					ReportVersionDto rvdto = new ReportVersionDto();
					RoleVersion roleVersion = roleVersionService.findById(Long.valueOf(rv.getIdVersionCapacidades()));
					rvdto.setRoleVersion(roleVersion == null ? null
							: new RoleVersionDto(roleVersion.getId(), roleVersion.getIdTipoInterfaz(),
									roleVersion.getFechaImportacion(), roleVersion.getNumRegistros(),
									roleVersion.getNombreFichero(), roleVersion.getDescripcion(),
									roleVersion.getUsuario()));
					StaffingVersion staffingVersion = staffingVersionService
							.findById(Long.valueOf(rv.getIdVersionStaffing()));
					rvdto.setStaffingVersion(staffingVersion == null ? null
							: new StaffingVersionDto(staffingVersion.getId(), staffingVersion.getIdTipoInterfaz(),
									staffingVersion.getFechaImportacion(), staffingVersion.getNumRegistros(),
									staffingVersion.getNombreFichero(), staffingVersion.getDescripcion(),
									staffingVersion.getUsuario()));
					CertificatesVersion certificatesVersion = certificatesService.findById(Long.valueOf(rv.getIdVersionCertificaciones()));
					rvdto.setCertificatesVersion(certificatesVersion == null ? null
							: new CertificatesVersionDto(certificatesVersion.getId(), certificatesVersion.getIdTipoInterfaz(),
									certificatesVersion.getFechaImportacion(), certificatesVersion.getNumRegistros(),
									certificatesVersion.getNombreFichero(), certificatesVersion.getDescripcion(),
									certificatesVersion.getUsuario()));
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

	/**
	 * Retrieves a {@link ReportVersion} by its ID.
	 * 
	 * <p>This method fetches a single {@link ReportVersion} entity from the database based on the provided ID.</p>
	 *
	 * @param id The ID of the report version to retrieve. This should be a valid identifier for a {@link ReportVersion}.
	 * @return The {@link ReportVersion} entity associated with the given ID.
	 */
	@GetMapping("/{id}")
	public ReportVersion findById(@PathVariable String id) {
		return this.reportVersionService.findByIdVersionCapacidades(Long.valueOf(id));
	}

	/**
	 * Retrieves a list of {@link ReportVersion} entities based on the screenshot number and optional year.
	 * 
	 * <p>This method filters the {@link ReportVersion} entities to match the provided screenshot number. If a year is provided,
	 * it further filters the results to include only those versions from the specified year.</p>
	 *
	 * @param screenshot The screenshot number to filter by. This should be a valid screenshot identifier.
	 * @param year (Optional) The year to filter the report versions by. If not provided, no year-based filtering is applied.
	 * @return A list of {@link ReportVersion} entities that match the provided screenshot number and optional year.
	 */
	@GetMapping("/screenshot/{screenshot}")
	public List<ReportVersion> findByScreenshotNum(
			@PathVariable(name = "screenshot", required = true) String screenshot,
			@RequestParam(value = "year", required = false) String year) {
		return this.reportVersionService.findByScreenshot(screenshot, year);
	}

	/**
	 * Retrieves a list of years available in the report versions.
	 * 
	 * <p>This method returns a list of distinct years from the report versions. If a screenshot number is provided,
	 * it filters the years to include only those associated with the given screenshot.</p>
	 *
	 * @param screenshot (Optional) The screenshot number to filter the years by. If not provided, all available years are returned.
	 * @return A list of years represented as strings, which are present in the report versions.
	 */
	@GetMapping("/years")
	public List<String> findYears(@RequestParam(name = "screenshot", required = false) String screenshot) {
		return this.reportVersionService.findYears(screenshot);
	}

	/**
	 * Saves or updates a {@link ReportVersion} based on the provided ID and DTO.
	 * 
	 * <p>This method updates an existing {@link ReportVersion} entity if an ID is provided or creates a new entity if no ID is provided.
	 * The details of the report version are specified in the provided {@link ReportVersionDto} object.</p>
	 *
	 * @param id The ID of the report version to update. If not provided, a new report version will be created.
	 * @param dto The {@link ReportVersionDto} containing the data to save or update.
	 */
	@PutMapping({ "/{id}" })
	public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ReportVersionDto dto) {
		this.reportVersionService.save(id, dto);
	}

	/**
	 * Generates a new {@link ReportVersion} based on the provided report generation data.
	 * 
	 * <p>This method creates a new {@link ReportVersion} entity using the details specified in the {@link GenerateReportVersionDto}.
	 * The generated report version is returned as the result of this operation.</p>
	 *
	 * @param dto The {@link GenerateReportVersionDto} containing the data needed to generate the new report version.
	 * @return The newly generated {@link ReportVersion} entity.
	 */
	@PostMapping(value = "/generate-report", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReportVersion generateReport(@RequestBody GenerateReportVersionDto dto) {
		return this.reportVersionService.generateReport(dto);
	}
}
