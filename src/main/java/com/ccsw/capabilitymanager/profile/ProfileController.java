package com.ccsw.capabilitymanager.profile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.profile.model.InformeRoles;
import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(value = "/profile")
@RestController
public class ProfileController {

	@Autowired
	private ProfileService profileService;

	@Autowired
	private ExportService exportService;

	
	@Autowired
	DozerBeanMapper mapper;

	/**
	 * Retrieves and groups profile data from the database based on the specified report ID.
	 *
	 * <p>This method fetches profile data associated with the given `idReport`, groups the profiles by their
	 * "actual" status, and then further groups each of these by their "perfil" type. It returns a nested map
	 * where the outer map's keys represent the "actual" status, and the values are maps where the keys represent
	 * the "perfil" types, with the associated values being the count of profiles for each combination.</p>
	 *
	 * @param idReport The ID of the report used to filter the profiles to be retrieved.
	 * @return A nested map where the outer map's keys are "actual" statuses, each mapping to another map where
	 *         keys are "perfil" types and values are the counts of profiles for each type.
	 */
	@GetMapping("/db")
	public Map<String, Map<String, Long>> findAllDb(@RequestParam(value = "idReport", required = true) int idReport) {
		return this.profileService.findAll(idReport).stream().collect(Collectors.groupingBy(Profile::getActual, Collectors.groupingBy(Profile::getPerfil, Collectors.counting())));
	}

	/**
	 * Retrieves the list of profile totals based on the specified ID and report ID.
	 *
	 * <p>This method fetches profile totals for a given profile identifier (`id`) and report identifier (`idReport`).
	 * It returns a list of {@link ProfileTotal} objects that contain aggregated information about profiles for the
	 * specified parameters.</p>
	 *
	 * @param id The profile identifier used to filter the profile totals.
	 * @param idReport The report ID used to filter the profile totals.
	 * @return A list of {@link ProfileTotal} objects representing the aggregated profile totals for the specified
	 *         profile identifier and report ID.
	 */
	@GetMapping("/profiletotals/{id}")
	public List<ProfileTotal> findAllProfileTotals(@PathVariable String id,
			@RequestParam(value = "idReport", required = true) int idReport) {
		return this.profileService.findAllProfileTotals(id, idReport);
	}

	/**
	 * Retrieves the report of roles based on the specified report ID.
	 *
	 * <p>This method fetches an {@link InformeRoles} object that contains information about roles related to the
	 * specified report ID (`idReport`). The returned object includes aggregated role data relevant to the given
	 * report.</p>
	 *
	 * @param idReport The report ID used to fetch the roles information.
	 * @return An {@link InformeRoles} object containing the aggregated roles information for the specified report ID.
	 */
	@GetMapping("/informeRoles")
	public InformeRoles findAllInformeRoles(@RequestParam(value = "idReport", required = true) int idReport) {
		return this.profileService.findAllInformeRoles(idReport);
	}

	/**
	 * Exports profile totals data as a CSV file for the specified profile and report ID.
	 *
	 * <p>This method retrieves profile totals based on the provided profile ID and report ID, then exports
	 * the data to a CSV file which is written to the HTTP response. The CSV file is dynamically generated and
	 * attached to the response for download.</p>
	 *
	 * @param servletResponse The {@link HttpServletResponse} used to write the CSV file to the client.
	 * @param id The profile ID used to fetch the profile totals.
	 * @param idReport The report ID used to filter the profile totals.
	 * @throws IOException If an input or output exception occurs during the writing of the CSV file.
	 */
	@RequestMapping(path = "/profiletotals/{id}/csv", method = RequestMethod.GET)
	public void findAllProfileTotalsCsv(HttpServletResponse servletResponse, @PathVariable String id,
			@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
		exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idReport));
		exportService.writeProfileTotalsToCsv(id, servletResponse);
	}


	/**
	 * Exports profile totals data as an Excel file for the specified profile and report ID.
	 *
	 * <p>This method retrieves profile totals based on the provided profile ID and report ID, then exports
	 * the data to an Excel file which is written to the HTTP response. The Excel file is dynamically generated and
	 * attached to the response for download.</p>
	 *
	 * @param servletResponse The {@link HttpServletResponse} used to write the Excel file to the client.
	 * @param id The profile ID used to fetch the profile totals.
	 * @param idReport The report ID used to filter the profile totals.
	 * @throws IOException If an input or output exception occurs during the writing of the Excel file.
	 */
	@RequestMapping(path = "/profiletotals/{id}/excel", method = RequestMethod.GET)
	public void findAllProfileTotalsExcel(HttpServletResponse servletResponse, @PathVariable String id,
			@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
		exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idReport));
		exportService.writeProfileTotalsToExcel(id, servletResponse);
	}

	/**
	 * Exports a list of profiles as an Excel file for the specified profile and report ID.
	 *
	 * <p>This method retrieves a list of profiles based on the provided profile ID and report ID, and then exports
	 * the data to an Excel file which is written to the HTTP response. The Excel file is dynamically generated and
	 * attached to the response for download.</p>
	 *
	 * @param servletResponse The {@link HttpServletResponse} used to write the Excel file to the client.
	 * @param id The profile ID used to fetch the list of profiles.
	 * @param idReport The report ID used to filter the profiles.
	 * @throws IOException If an input or output exception occurs during the writing of the Excel file.
	 */
	@RequestMapping(path = "/profilelist/{id}/excel", method = RequestMethod.GET)
	public void findAllProfileExcel(HttpServletResponse servletResponse, @PathVariable String id,
			@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
		exportService.setProfileGroup(this.profileService.findAllProfile(id, idReport));
		exportService.writeProfileToExcel(id, servletResponse, Long.valueOf(idReport));
	}

	/**
	 * Exports a list of profiles as an XLS file for the specified profile and report ID.
	 *
	 * <p>This method retrieves a list of profiles based on the provided profile ID and report ID, and then exports
	 * the data to an XLS file which is written to the HTTP response. The XLS file is dynamically generated and
	 * attached to the response for download.</p>
	 *
	 * @param servletResponse The {@link HttpServletResponse} used to write the XLS file to the client.
	 * @param id The profile ID used to fetch the list of profiles.
	 * @param idReport The report ID used to filter the profiles.
	 * @throws IOException If an input or output exception occurs during the writing of the XLS file.
	 */
	@RequestMapping(path = "/profilelist/{id}/xls", method = RequestMethod.GET)
	public void findAllProfileXls(HttpServletResponse servletResponse, @PathVariable String id,
			@RequestParam(value = "idReport", required = true) int idReport)  throws IOException {
		exportService.setProfileGroup(this.profileService.findAllProfile(id, idReport));
		exportService.writeProfileToTemplateExcel(id, servletResponse);
	}

}
