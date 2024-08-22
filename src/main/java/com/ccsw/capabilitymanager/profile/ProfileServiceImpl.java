package com.ccsw.capabilitymanager.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.config.literal.LiteralService;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.graderole.GradeRoleService;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;
import com.ccsw.capabilitymanager.profile.model.InformeRoles;
import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.profile.model.ProfileGroup;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;
import com.ccsw.capabilitymanager.reportversion.ReportVersionService;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.views.service.CounterSummaryService;
import com.ccsw.capabilitymanager.views.service.ViewGradosRolesService;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {
	private static final String ERROR_INIT = ">>> [ERROR][ProfileServiceImpl] (";
	@Autowired
	private LiteralService literalService;

	@Autowired
	private GradeRoleService gradeRoleService;

	@Autowired
	private ReportVersionService reportVersionService;

	@Autowired
	private CounterSummaryService counterSummaryService;

	@Autowired
	private ViewGradosRolesService viewGradosRolesService;
	
	/**
	 * Retrieves a list of all profiles for the given report ID, with caching.
	 *
	 * <p>This method first retrieves the {@link ReportVersion} for the specified report ID. It then uses the 
	 * {@link CounterSummaryService} to recover the counter summaries associated with the versions of capacities and 
	 * staffing obtained from the report version. The resulting list of profiles is returned and cached to optimize 
	 * performance for repeated requests.</p>
	 *
	 * @param idReport The ID of the report used to fetch the corresponding {@link ReportVersion}.
	 * @return A list of {@link Profile} objects associated with the specified report ID.
	 */
	@Cacheable("findAll")
	@Override
	public List<Profile> findAll(int idReport) {
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
		return this.counterSummaryService.recoverCounterSummaryAll(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing()).stream()
				.toList();
	}

	/**
	 * Retrieves a list of profiles that match the specified status and report ID, with caching.
	 *
	 * <p>This method first retrieves the {@link ReportVersion} associated with the given report ID. It then uses 
	 * the {@link CounterSummaryService} to recover counter summaries filtered by the specified status (actual) and 
	 * the versions of capacities and staffing obtained from the report version. The resulting list of profiles is 
	 * returned and cached to enhance performance for repeated requests.</p>
	 *
	 * @param actual The status used to filter profiles.
	 * @param idReport The ID of the report used to fetch the corresponding {@link ReportVersion}.
	 * @return A list of {@link Profile} objects that match the specified status and are associated with the 
	 *         specified report ID.
	 */
	@Cacheable("findAllActual")
	public List<Profile> findAllActual(String actual, int idReport) {
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
		return this.counterSummaryService.recoverCounterSummary(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing(), actual).stream()
				.toList();
	}
	
	/**
	 * Obtains the certifications version associated with the specified report ID.
	 *
	 * <p>This method retrieves the {@link ReportVersion} corresponding to the provided report ID. It then uses
	 * the {@link CounterSummaryService} to obtain the certifications version details based on the version ID
	 * retrieved from the report version.</p>
	 *
	 * @param idReport The ID of the report used to fetch the corresponding {@link ReportVersion}.
	 */
	public void obtenerVersionCertificaciones(int idReport) {
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
		counterSummaryService.obtenerVersionCertificaciones(rv.getIdVersionCertificaciones());
	}

	/**
	 * Retrieves a comprehensive report of roles and grade totals for a specific report ID.
	 *
	 * <p>This method generates an {@link InformeRoles} object containing detailed role information by fetching
	 * profile totals for various roles from the {@link ProfileService} and grade totals from the {@link GradeRoleService}.
	 * The roles include Engagement Managers, Architects, Software Engineers, Industry Experts, Business Analysts,
	 * and specific categories such as Architects & SE Custom Apps Development and Architects & SE Integration & APIs.</p>
	 *
	 * @param idReport The ID of the report used to fetch profile totals and grade totals.
	 * @return An {@link InformeRoles} object populated with lists of {@link ProfileTotal} for different roles
	 *         and a list of {@link GradeTotal}.
	 */
	@Override
	public InformeRoles findAllInformeRoles(int idReport) {

		InformeRoles informeRoles = new InformeRoles();
		List<ProfileTotal> architects = new ArrayList<ProfileTotal>();
		List<ProfileTotal> softwareEngineer = new ArrayList<ProfileTotal>();
		List<ProfileTotal> industryExperts = new ArrayList<ProfileTotal>();
		List<ProfileTotal> engagementManagers = new ArrayList<ProfileTotal>();
		List<ProfileTotal> businessAnalyst = new ArrayList<ProfileTotal>();
		List<ProfileTotal> architectsCustomApps = new ArrayList<ProfileTotal>();
		List<ProfileTotal> architectsIntegration = new ArrayList<ProfileTotal>();
		List<GradeTotal> gradeTotal = new ArrayList<GradeTotal>();
		
		engagementManagers = findAllProfileTotals("Engagement Managers", idReport);
		architects = findAllProfileTotals("Architects", idReport);
		softwareEngineer = findAllProfileTotals("Software Engineer", idReport);
		industryExperts = findAllProfileTotals("Industry Experts", idReport);
		businessAnalyst = findAllProfileTotals("Business Analyst", idReport);
		architectsCustomApps = findAllProfileTotals("Architects & SE Custom Apps Development", idReport);
		architectsIntegration = findAllProfileTotals("Architects & SE Integration & APIs", idReport);

		gradeTotal = gradeRoleService.findAllGradeTotals(idReport);
		informeRoles.setArchitects(architects);
		informeRoles.setSoftwareEngineer(softwareEngineer);
		informeRoles.setIndustryExperts(industryExperts);
		informeRoles.setEngagementManagers(engagementManagers);
		informeRoles.setBusinessAnalyst(businessAnalyst);
		informeRoles.setArchitectsCustomApps(architectsCustomApps);
		informeRoles.setArchitectsIntegration(architectsIntegration);
		informeRoles.setGradeTotal(gradeTotal);
		return informeRoles;
	}

	/**
	 * Retrieves a list of {@link ProfileTotal} objects based on the specified role or type and report ID.
	 *
	 * <p>This method fetches and aggregates profile totals for various roles or categories by first retrieving
	 * the relevant profile data based on the role type specified by the `id` parameter. It also handles special
	 * cases such as grade totals and combined totals for all profiles.</p>
	 *
	 * <p>The method utilizes caching to improve performance by storing the results of previous queries. The caching
	 * is based on the method name and parameters.</p>
	 *
	 * @param id The identifier for the role or category. Valid values include:
	 *           <ul>
	 *               <li>"Engagement Managers"</li>
	 *               <li>"Architects"</li>
	 *               <li>"Business Analyst"</li>
	 *               <li>"Software Engineer"</li>
	 *               <li>"Industry Experts"</li>
	 *               <li>"Architects & SE Custom Apps Development"</li>
	 *               <li>"Architects & SE Integration & APIs"</li>
	 *               <li>"Pyramid Grade-Rol"</li>
	 *               <li>"All"</li>
	 *           </ul>
	 *           For any other value, an exception is thrown.
	 * @param idReport The ID of the report used to fetch profile data and perform the aggregation.
	 * @return A list of {@link ProfileTotal} objects that represents the aggregated data for the specified role or category.
	 * @throws MyBadAdviceException If the provided role identifier (`id`) is not valid.
	 */
	@Override
	@Cacheable("findAllProfileTotals")
	public List<ProfileTotal> findAllProfileTotals(String id, int idReport) {

		List<Profile> listAll = new ArrayList<Profile>();
		List<Profile> listActual = new ArrayList<Profile>();
		List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype(id, "r");
		switch (id) {
		case "Engagement Managers":
			obtenerVersionCertificaciones(idReport);
			listActual = findAllActual(id, idReport);
			return engagementManagersTotal(findByTypeAndSubtype, listActual);
		case "Architects":
			obtenerVersionCertificaciones(idReport);
			listActual = findAllActual(id, idReport);
			return architectsTotal(findByTypeAndSubtype, listActual);
		case "Business Analyst":
			listActual = findAllActual(id, idReport);
			return businessAnalystTotal(findByTypeAndSubtype, listActual);
		case "Software Engineer":
			listActual = findAllActual(id, idReport);
			return softwareEngineerTotal(findByTypeAndSubtype, listActual);
		case "Industry Experts":
			listAll = this.findAll(idReport);
			return industryExpertsTotal(findByTypeAndSubtype, listAll);
		case "Architects & SE Custom Apps Development":
			listAll = this.findAll(idReport);
			return architectsAndSECustomAppsDevelopmentTotal(findByTypeAndSubtype, listAll);
		case "Architects & SE Integration & APIs":
			listAll = this.findAll(idReport);
			return architectsAndSEIntegrationAndApisTotal(findByTypeAndSubtype, listAll);
		case "Pyramid Grade-Rol":
			return pyramidTotal(this.gradeRoleService.findAllGradeTotals(idReport));
		case "All":
			listAll = this.findAll(idReport);
			return allTotal(findByTypeAndSubtype, listAll);
		default:
			CapabilityLogger.logError(ERROR_INIT + "findAllProfileTotals) : Entrada no válida");
			throw new MyBadAdviceException("entrada no válida");
		}
	}
	
	/**
	 * Calculates total profile statistics for Engagement Managers based on provided profile data and literal descriptions.
	 *
	 * <p>This method generates profile statistics specifically for Engagement Managers roles by filtering the profiles
	 * and calculating totals based on experience and certification criteria. It creates a {@link ProfileTotal} object 
	 * for each relevant profile description from {@link Literal} and returns a list of these objects.</p>
	 *
	 * <p>The method performs the following calculations:</p>
	 * <ul>
	 *   <li>Counts the total number of profiles matching "Engagement Managers".</li>
	 *   <li>Counts profiles with specific experience and certification criteria:</li>
	 *     <ul>
	 *       <li>Experience in complex solutions.</li>
	 *       <li>Experience in Agile.</li>
	 *       <li>Certifications including SAFE, EM’s Certification Levels 1 to 4.</li>
	 *     </ul>
	 *   <li>Counts profiles also matching "PMO" and "Scrum" profiles, aggregating totals accordingly.</li>
	 * </ul>
	 *
	 * <p>The method creates two {@link ProfileTotal} objects:</p>
	 * <ol>
	 *   <li>The first object represents profiles matching "Engagement Managers".</li>
	 *   <li>The second object aggregates profiles matching "PMO" and "Scrum", combining totals from both categories.</li>
	 * </ol>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects where each {@link Literal} contains a description 
	 *                             used to label the profile categories. Descriptions are used as the profile names 
	 *                             in the resulting statistics.
	 * @param list A list of {@link Profile} objects that will be analyzed to count the total number of profiles 
	 *             for each category based on the filtering criteria.
	 * @return A list of {@link ProfileTotal} objects where each object represents:
	 */
	private List<ProfileTotal> engagementManagersTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<Profile> listEM = list.stream().filter(p->p.getPerfil().contains("Engagement Managers")).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listEM.size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("SAFE")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 1")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 2")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 3")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 4")).toList().size()));
		ProfileTotal profileTotal = new ProfileTotal();
		profileTotal.setProfile(findByTypeAndSubtype.get(0).getDesc());
		profileTotal.setTotals(totals);

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		profileTotalList.add(profileTotal);
		profileTotal = new ProfileTotal();
		totals = new ArrayList<Long>();
		listEM = list.stream().filter(p->p.getPerfil().toUpperCase().contains("PMO")).toList();
		totals.add(Long.valueOf(listEM.size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("SAFE")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 1")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 2")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 3")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM’s Certification Level 4")).toList().size()));
		listEM = list.stream().filter(p->p.getPerfil().contains("Scrum")).toList();
		totals.set(0, totals.get(0) + Long.valueOf(listEM.size()));
		totals.set(1, totals.get(1) + Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
		totals.set(2, totals.get(2) + Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
		
		profileTotal.setProfile(findByTypeAndSubtype.get(1).getDesc());
		profileTotal.setTotals(totals);
		profileTotalList.add(profileTotal);
		return profileTotalList;
	}

	
	/**
	 * Calculates and aggregates total profile statistics for Engagement Managers and related roles.
	 *
	 * <p>This method computes various statistics related to Engagement Managers and similar roles based on
	 * profile data. It categorizes the profiles into Engagement Managers, PMO, and Scrum roles, and then counts
	 * specific attributes such as experience and certifications for each category.</p>
	 *
	 * <p>The method produces two main aggregations:
	 * <ul>
	 *   <li>Total profiles and counts of specific experiences and certifications for Engagement Managers.</li>
	 *   <li>Total profiles and counts of specific experiences and certifications for PMO and Scrum roles combined.</li>
	 * </ul></p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects containing description details used to label the
	 *                             profile categories. The descriptions are used for setting profile names in the results.
	 * @param list A list of {@link Profile} objects to be analyzed. Each profile contains information about experience
	 *             and certifications that will be counted and categorized.
	 * @return A list of {@link ProfileTotal} objects, where each object contains:
	 */
	private List<ProfileTotal> architectsTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			List<Profile> listArchitects = list.stream().filter(p->p.getPerfil().contains(literal.getDesc())).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(listArchitects.size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("SAFE")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("TOGAF")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("Arquitect Certification Level 1: Certified Architect")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("Arquitect Certification Level 2: Certified Architect")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("Arquitect Certification Level 3: Certified Architect")).toList().size()));
			totals.add(Long.valueOf(listArchitects.stream().filter(p->p.getCertificaciones().contains("Arquitect Certification Level 4: Certified Architect")).toList().size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}

		return profileTotalList;
	}

	/**
	 * Calculates total profile statistics for Business Analyst roles based on provided profile data and literal descriptions.
	 *
	 * <p>This method generates profile statistics for Business Analyst roles using the provided list of profiles and 
	 * descriptions from the {@link Literal} objects. Each description is used to create a {@link ProfileTotal} object 
	 * with the total count of profiles matching that description.</p>
	 *
	 * <p>The method produces a list of {@link ProfileTotal} objects, each representing a different profile description 
	 * and containing the total count of profiles for that description. Currently, the method only aggregates the total 
	 * number of profiles for each description without further breakdown.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects where each {@link Literal} contains a description 
	 *                             used to label the profile categories. Each description will be used as the profile name 
	 *                             in the resulting statistics.
	 * @param list A list of {@link Profile} objects that will be analyzed to count the total number of profiles for each
	 *             description.
	 * @return A list of {@link ProfileTotal} objects where each object represents:
	 */
	private List<ProfileTotal> businessAnalystTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(list.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}

		return profileTotalList;
	}

	/**
	 * Calculates total profile statistics for Software Engineer roles based on provided profile data and literal descriptions.
	 *
	 * <p>This method generates profile statistics for Software Engineer roles using the provided list of profiles and 
	 * descriptions from the {@link Literal} objects. Each description is used to create a {@link ProfileTotal} object 
	 * with the total count of profiles matching that description.</p>
	 *
	 * <p>The method produces a list of {@link ProfileTotal} objects, each representing a different profile description 
	 * and containing the total count of profiles for that description. Currently, the method only aggregates the total 
	 * number of profiles for each description without further breakdown.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects where each {@link Literal} contains a description 
	 *                             used to label the profile categories. Each description will be used as the profile name 
	 *                             in the resulting statistics.
	 * @param list A list of {@link Profile} objects that will be analyzed to count the total number of profiles for each
	 *             description.
	 * @return A list of {@link ProfileTotal} objects where each object represents:
	 */
	private List<ProfileTotal> softwareEngineerTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(list.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}

		return profileTotalList;
	}

	/**
	 * Generates a list of {@link ProfileTotal} objects that contain the total count of profiles for each group
	 * defined in the {@link Literal} list. Each group will have a total count equal to the size of the entire profile list.
	 *
	 * <p>This method iterates over each {@link Literal} object to create a {@link ProfileTotal} for each group description.
	 * The total count is calculated based on the size of the provided profile list.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a description 
	 *                             for a group of profiles.
	 * @param list A list of {@link Profile} objects used to calculate the total number of profiles for each group.
	 * @return A list of {@link ProfileTotal} objects, each representing a group with its total profile count.
	 */
	private List<ProfileTotal> allTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(list.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}

		return profileTotalList;
	}

	/**
	 * Generates a list of {@link ProfileTotal} objects that provide totals for different industry sectors based on the 
	 * profiles in the provided list. Each {@link ProfileTotal} contains counts of profiles with experience in various 
	 * industry sectors such as "Consumer", "Energy & Utilities", "Manufacturing", "Life Science", "Public Sector", 
	 * "Telco", and "Financial".
	 *
	 * <p>This method calculates the total number of profiles in each sector and provides a sum of all profiles across 
	 * these sectors.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a description 
	 *                             for a group of profiles.
	 * @param list A list of {@link Profile} objects used to calculate the total number of profiles for each industry sector.
	 * @return A list of {@link ProfileTotal} objects, each representing a group with totals for different industry sectors.
	 */
	private List<ProfileTotal> industryExpertsTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			List<Profile> listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Consumer")).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(0L);
			Long sum = Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Energy & Utilities")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Manufacturing")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Life Science")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Public Sector")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Telco")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Financial")).toList();
			sum=sum+Long.valueOf(listIndustryExperts.size());
			totals.add(Long.valueOf(listIndustryExperts.size()));
			totals.set(0, sum);
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}
		return profileTotalList;
	}

	/**
	 * Generates a list of {@link ProfileTotal} objects that summarize the total count of profiles for different technology 
	 * stacks and cloud skills among Architects and Software Engineers. Profiles are grouped based on their role and 
	 * specialization with a breakdown of their technical skills, including various backend technologies, frontend, 
	 * mainframe, and cloud skills.
	 *
	 * <p>This method iterates over each {@link Literal} object to create a {@link ProfileTotal} for each group description. 
	 * It calculates totals based on the role ("Architects" or "Software Engineer") and their technical skills.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a description 
	 *                             for a group of profiles.
	 * @param list A list of {@link Profile} objects used to calculate the total number of profiles based on their role 
	 *             and technical skills.
	 * @return A list of {@link ProfileTotal} objects, each representing a group with detailed counts for various technology 
	 */
	private List<ProfileTotal> architectsAndSECustomAppsDevelopmentTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (int i = 0; i < findByTypeAndSubtype.toArray().length; i++) {
			String actual = i == 0 ? Constants.ROLL1_AR : Constants.ROLL1_SE;
			String perfil = i==0?"Solution":"SE";
			List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(0L);
			Long suma = 0L;
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil))
					.filter(p -> p.getTecnicoSolution().toLowerCase().contains("backend java")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil))
					.filter(p -> (p.getTecnicoSolution().toLowerCase().contains("backend .net") || p.getTecnicoSolution().contains("back end .net"))).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("stack java")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("stack .net")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("frontend")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("mainframe")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("devops automation")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnicoSolution().toLowerCase().contains("otro")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			suma +=listArchitects.size();
			totals.set(0, suma);

			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().toLowerCase().contains("aws")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().toLowerCase().contains("azure")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().toLowerCase().contains("gcp")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().toLowerCase().contains("other clouds")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().toLowerCase().contains("outsystems")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().toLowerCase().contains("powerapps")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().toLowerCase().contains("mendix")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().toLowerCase().contains("other")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(findByTypeAndSubtype.get(i).getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}


		return profileTotalList;
	}

	/**
	 * Generates a list of {@link ProfileTotal} objects that provide totals for different integration technologies 
	 * used by Architects and Software Engineers. The method calculates the number of profiles for each integration 
	 * technology (Mulesoft, Boomi, Software AG, Tibco, other vendors, and open source) for each group defined in the 
	 * {@link Literal} list. The total count of profiles for each group is also computed.
	 *
	 * <p>This method iterates over each {@link Literal} object to create a {@link ProfileTotal} for each group description.
	 * It filters the profiles based on their role and specialization, then further filters by specific integration technologies
	 * and sums the counts for each category.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a description 
	 *                             for a group of profiles (e.g., Architects or Software Engineers).
	 * @param list A list of {@link Profile} objects used to calculate the number of profiles for each integration technology
	 *             and the total count within the group.
	 * @return A list of {@link ProfileTotal} objects, each representing a group with detailed counts for various integration
	 *         technologies.
	 */
	private List<ProfileTotal> architectsAndSEIntegrationAndApisTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (int i = 0; i < findByTypeAndSubtype.toArray().length; i++) {
			String actual = i == 0 ? Constants.ROLL1_AR : Constants.ROLL1_SE;
			String perfil = i==0?"Integration":"SE";
			List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			Long total = 0L;
			totals.add(Long.valueOf(0));
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil)).filter(p -> p.getTecnicoIntegration().toLowerCase().contains("mulesoft"))
					.toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil)).filter(p -> p.getTecnicoIntegration().toLowerCase().contains("boomi"))
					.toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil))
					.filter(p -> p.getTecnicoIntegration().toLowerCase().contains("software ag")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil)).filter(p -> p.getTecnicoIntegration().toLowerCase().contains("tibco"))
					.toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil))
					.filter(p -> p.getTecnicoIntegration().toLowerCase().contains("other vendor")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			listArchitects = list.stream().filter(p -> p.getActual().equals(actual)).filter(p -> p.getPerfil().contains(perfil))
					.filter(p -> p.getTecnicoIntegration().toLowerCase().contains("open source")).toList();
			totals.add(Long.valueOf(listArchitects.size()));
			total +=listArchitects.size();
			totals.set(0, total);
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(findByTypeAndSubtype.get(i).getDesc());
			profileTotal.setTotals(totals);
			profileTotalList.add(profileTotal);
		}
		return profileTotalList;
	}

	/**
	 * Converts a list of {@link GradeTotal} objects into a list of {@link ProfileTotal} objects.
	 * For each {@link GradeTotal}, this method calculates the total sum of counts across all categories,
	 * adds this total as the first element in the totals list, and then creates a corresponding {@link ProfileTotal}.
	 *
	 * <p>The method processes each {@link GradeTotal} by:
	 * <ul>
	 *   <li>Setting the profile name in {@link ProfileTotal} to the grade name from {@link GradeTotal}.</li>
	 *   <li>Calculating the sum of all totals present in the {@link GradeTotal} object.</li>
	 *   <li>Inserting this sum as the first element in the totals list of the {@link ProfileTotal} object.</li>
	 *   <li>Adding the modified {@link ProfileTotal} object to the result list.</li>
	 * </ul>
	 * The final list contains {@link ProfileTotal} objects with updated totals where the first value represents
	 * the aggregate sum of all other totals for each grade.</p>
	 *
	 * @param list A list of {@link GradeTotal} objects, where each object contains a grade name and a list of totals.
	 *             The totals represent counts across different categories or criteria.
	 * @return A list of {@link ProfileTotal} objects. Each {@link ProfileTotal} object represents a grade and includes:
	 */
	private List<ProfileTotal> pyramidTotal(List<GradeTotal> list) {

		List<ProfileTotal> profileTotalList = new ArrayList<>();
		for (GradeTotal gradeTotal : list) {
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(gradeTotal.getGrade());
			Long suma=0L;
			for (Long total : gradeTotal.getTotals()) {
				suma=suma+total;
			}
			gradeTotal.getTotals().add(0, suma);
			profileTotal.setTotals(gradeTotal.getTotals());
			profileTotalList.add(profileTotal);

		}
		return profileTotalList;
	}

	/**
	 * Retrieves a list of {@link ProfileGroup} based on the specified profile type and report ID.
	 * 
	 * <p>This method fetches all profiles and a subset of profiles based on their actual status. It then
	 * retrieves a list of literals and uses them to determine the appropriate profile groups based on
	 * the provided profile type ID. Depending on the profile type, it invokes specific methods to generate
	 * the desired profile groups.</p>
	 * 
	 * <p>The method handles the following profile types:</p>
	 * <ul>
	 *   <li>"Engagement Managers" - Calls {@link #engagementManagers(List, List)}.</li>
	 *   <li>"Architects" - Calls {@link #architects(List, List)}.</li>
	 *   <li>"Business Analyst" - Calls {@link #businessAnalyst(List, List)}.</li>
	 *   <li>"Software Engineer" - Calls {@link #softwareEngineer(List, List)}.</li>
	 *   <li>"Industry Experts" - Calls {@link #industryExperts(List, List)}.</li>
	 *   <li>"Architects & SE Custom Apps Development" - Calls {@link #architectsAndSECustomAppsDevelopment(List, List)}.</li>
	 *   <li>"Architects & SE Integration & APIs" - Calls {@link #architectsAndSEIntegrationAndApis(List, List)}.</li>
	 *   <li>"Pyramid Grade-Rol" - Calls {@link #pyramid(List, List, int)}.</li>
	 *   <li>"All Profiles" - Calls {@link #allProfiles(List, List)}.</li>
	 * </ul>
	 * 
	 * <p>If the profile type ID does not match any known type, an error is logged and a {@link MyBadAdviceException} is thrown.</p>
	 * 
	 * @param id The profile type ID which determines the type of profile group to retrieve. Expected values are
	 *           specific profile types like "Engagement Managers", "Architects", "Business Analyst", etc.
	 * @param idReport The report ID used to fetch profile data and additional details required for the profile groups.
	 * @return A list of {@link ProfileGroup} objects corresponding to the requested profile type.
	 * @throws MyBadAdviceException If the provided profile type ID is invalid.
	 */
	@Override
	public List<ProfileGroup> findAllProfile(String id, int idReport) {

		List<Profile> listAll = this.findAll(idReport);
		List<Profile> listActual = findAllActual(id, idReport);
		obtenerVersionCertificaciones(idReport);
		List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype(id, "r");
		switch (id) {
		case "Engagement Managers":
			return engagementManagers(findByTypeAndSubtype, listActual);
		case "Architects":
			return architects(findByTypeAndSubtype, listActual);
		case "Business Analyst":
			return businessAnalyst(findByTypeAndSubtype, listActual);
		case "Software Engineer":
			return softwareEngineer(findByTypeAndSubtype, listActual);
		case "Industry Experts":
			return industryExperts(findByTypeAndSubtype, listAll);
		case "Architects & SE Custom Apps Development":
			return architectsAndSECustomAppsDevelopment(findByTypeAndSubtype, listAll);
		case "Architects & SE Integration & APIs":
			return architectsAndSEIntegrationAndApis(findByTypeAndSubtype, listAll);
		case "Pyramid Grade-Rol":
			return pyramid(findByTypeAndSubtype, listAll, idReport);
		case "All Profiles":
			return allProfiles(findByTypeAndSubtype, listAll);
		default:
			CapabilityLogger.logError(ERROR_INIT + "findAllProfileTotals): Entrada no válida.");
			throw new MyBadAdviceException("entrada no válida");
		}
	}

	private List<ProfileGroup> engagementManagers(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		ArrayList<ProfileGroup> profileList = new ArrayList<ProfileGroup>();
		List<Profile> listEM = list.stream().filter(p->p.getPerfil().contains("Engagement Managers")).toList();
		ProfileGroup profileGroup = new ProfileGroup();
		profileGroup.setGroup(findByTypeAndSubtype.get(0).getDesc());
		profileGroup.setProfile(listEM);
		profileList.add(profileGroup);

		profileGroup = new ProfileGroup();
		profileGroup.setGroup(findByTypeAndSubtype.get(1).getDesc());
		List<Profile> listPmo = list.stream().filter(p->p.getPerfil().contains("PMO")).toList();
		List<Profile> listScr = list.stream().filter(p->p.getPerfil().contains("Scrum Master")).toList();
		List<Profile> listSum  = new ArrayList<Profile>();
		listSum.addAll(listPmo);
		listSum.addAll(listScr);
		profileGroup.setProfile(listSum);
		profileList.add(profileGroup);
		return profileList;
	}

	private List<ProfileGroup> architects(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			List<Profile> listArchitects = list.stream().filter(p->p.getPerfil().contains(literal.getDesc())).toList();
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(literal.getDesc());
			profileGroup.setProfile(listArchitects);
			profileList.add(profileGroup);
		}

		return profileList;
	}

	private List<ProfileGroup> businessAnalyst(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(literal.getDesc());
			profileGroup.setProfile(list);
			profileList.add(profileGroup);
		}

		return profileList;
	}

	private List<ProfileGroup> softwareEngineer(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(literal.getDesc());
			profileGroup.setProfile(list);
			profileList.add(profileGroup);
		}
		return profileList;
	}

	private List<ProfileGroup> industryExperts(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			List<Profile> listIndustryExperts = new ArrayList<Profile>();
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Consumer")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Energy & Utilities")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Manufacturing")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Life Science")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Public Sector")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Telco")).toList());
			listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Financial Ser")).toList());
			HashSet<Profile> hashSet = new HashSet<Profile>(listIndustryExperts);
			listIndustryExperts.clear();
			listIndustryExperts.addAll(hashSet);
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(literal.getDesc());
			profileGroup.setProfile(listIndustryExperts);
			profileList.add(profileGroup);
		}
		return profileList;
	}

	/**
	 * Creates a list of {@link ProfileGroup} objects by grouping profiles based on their roles and specializations 
	 * such as "Architects" and "Software Engineers" with specific profiles related to "Solution" and "SE."
	 *
	 * <p>This method processes a list of {@link Literal} objects, where each {@link Literal} represents a group description 
	 * for specific profiles. It filters the profiles based on their role and specialization, creating a group for each 
	 * description provided in the {@link Literal} list.</p>
	 *
	 * <p>For each description in the {@link Literal} list, the method categorizes profiles into two main roles: 
	 * "Architects" and "Software Engineers." Within these roles, profiles are further filtered by their specialization 
	 * (e.g., "Solution" or "SE").</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a group description 
	 *                             for which profiles will be grouped. The descriptions are used to create groups of profiles.
	 * @param list A list of {@link Profile} objects that will be filtered and grouped based on their role and specialization.
	 * @return A list of {@link ProfileGroup} objects, where each {@link ProfileGroup} represents:
	 */
	private List<ProfileGroup> architectsAndSECustomAppsDevelopment(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (int i = 0; i < findByTypeAndSubtype.toArray().length; i++) {
			String actual = i==0?"Architects":"Software Engineer";
			String perfil = i==0?"Solution":"SE";
			List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(findByTypeAndSubtype.get(i).getDesc());
			profileGroup.setProfile(listArchitects);
			profileList.add(profileGroup);
		}

		return profileList;
	}

	
	/**
	 * Creates a list of {@link ProfileGroup} objects by grouping profiles based on their roles and specializations 
	 * such as "Architects" and "Software Engineer" with specific profiles like "Integration" and "SE".
	 *
	 * <p>This method processes a list of {@link Literal} objects, where each {@link Literal} represents a group description 
	 * for specific profiles. It filters the profiles based on their role and specialization, creating a group for each 
	 * description provided in the {@link Literal} list.</p>
	 *
	 * <p>For each description in the {@link Literal} list, the method categorizes profiles into two main roles: 
	 * "Architects" and "Software Engineers." Within these roles, profiles are further filtered by their specialization 
	 * (e.g., "Integration" or "SE").</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a group description 
	 *                             for which profiles will be grouped. The descriptions are used to create groups of profiles.
	 * @param list A list of {@link Profile} objects that will be filtered and grouped based on their role and specialization.
	 * @return A list of {@link ProfileGroup} objects, where each {@link ProfileGroup} represents:
	 *         <ul>
	 *           <li>A group of profiles filtered by role and specialization as defined by the descriptions in the 
	 *               {@link Literal} list.</li>
	 *         </ul>
	 *         Each {@link ProfileGroup} object contains:
	 *         <ul>
	 *           <li>The group name set to the description from the {@link Literal} list.</li>
	 *           <li>The list of profiles for that group, filtered based on their role ("Architects" or "Software Engineer") 
	 *               and specialization ("Integration" or "SE").</li>
	 *         </ul>
	 */
	private List<ProfileGroup> architectsAndSEIntegrationAndApis(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (int i = 0; i < findByTypeAndSubtype.toArray().length; i++) {
			String actual = i==0?"Architects":"Software Engineer";
			String perfil = i==0?"Integration":"SE";
			List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(findByTypeAndSubtype.get(i).getDesc());
			profileGroup.setProfile(listArchitects);
			profileList.add(profileGroup);
		}
		return profileList;
	}

	
	/**
	 * Creates a list of {@link ProfileGroup} objects by grouping profiles based on their grade and filtering them 
	 * according to the grade roles obtained from a report version.
	 *
	 * <p>This method constructs a list of {@link ProfileGroup} instances. Each group is created by filtering the profiles 
	 * based on their grade and ensuring that only those profiles are included that have corresponding roles in the 
	 * {@link GradeRole} collection obtained from the report version.</p>
	 *
	 * <p>For each grade description provided in the {@link Literal} list, a {@link ProfileGroup} is created. This group 
	 * initially contains all profiles with that grade. It then filters out profiles that do not have a corresponding 
	 * role in the {@link GradeRole} collection.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects, where each {@link Literal} contains a grade description 
	 *                             used to group profiles. Each description represents a grade for which profiles will be 
	 *                             grouped.
	 * @param list A list of {@link Profile} objects that will be grouped by their grade.
	 * @param idImport An identifier for the report version used to obtain the relevant {@link GradeRole} data.
	 * @return A list of {@link ProfileGroup} objects, where each {@link ProfileGroup} represents:
	 */
	private List<ProfileGroup> pyramid(List<Literal> findByTypeAndSubtype, List<Profile> list, int idImport) {

		List<ProfileGroup> profileList = new ArrayList<>();
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idImport));
		Collection<GradeRole> findGradeRoleAll = viewGradosRolesService.getAll(rv.getIdVersionCapacidades(),rv.getIdVersionStaffing());
		for (int i = 0; i < findByTypeAndSubtype.toArray().length; i++) {
			String grupo = findByTypeAndSubtype.get(i).getDesc();
			List<Profile> listGroup = list.stream().filter(p->p.getGrado().equals(grupo)).toList();
			List<Profile> listGrouptoRemove = new ArrayList<Profile>();
			listGrouptoRemove.addAll(listGroup);
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(grupo);
			profileGroup.setProfile(listGrouptoRemove);
			profileList.add(profileGroup);
			for (Profile profile : listGroup) {
				if (findGradeRoleAll.stream().filter(p->p.getId()==profile.getId()).count()==0) {
					listGrouptoRemove.removeIf(p->p.getId()==profile.getId());
				}
			}
		}
		return profileList;
	}

	/**
	 * Creates a list of {@link ProfileGroup} objects by grouping the provided profiles under each description in the given {@link Literal} list.
	 *
	 * <p>This method constructs a list of {@link ProfileGroup} instances, where each {@link ProfileGroup} contains
	 * all the profiles from the provided list, and is labeled according to each description found in the {@link Literal} list.</p>
	 *
	 * <p>Each {@link ProfileGroup} object is initialized with the description from the {@link Literal} list as its group name,
	 * and the entire profile list as its profiles.</p>
	 *
	 * @param findByTypeAndSubtype A list of {@link Literal} objects where each {@link Literal} contains a description 
	 *                             used to label the profile groups. Each description is used as the group name in the resulting {@link ProfileGroup} objects.
	 * @param list A list of {@link Profile} objects that will be assigned to each {@link ProfileGroup}.
	 * @return A list of {@link ProfileGroup} objects where each object represents:
	 */
	private List<ProfileGroup> allProfiles(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<ProfileGroup> profileList = new ArrayList<>();
		for (Literal literal : findByTypeAndSubtype) {
			ProfileGroup profileGroup = new ProfileGroup();
			profileGroup.setGroup(literal.getDesc());
			profileGroup.setProfile(list);
			profileList.add(profileGroup);
		}
		return profileList;
	}


}
