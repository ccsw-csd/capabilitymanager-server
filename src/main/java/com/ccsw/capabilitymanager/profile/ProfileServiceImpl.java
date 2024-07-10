package com.ccsw.capabilitymanager.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

	@Cacheable("findAll")
	@Override
	public List<Profile> findAll(int idReport) {
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
		return this.counterSummaryService.recoverCounterSummaryAll(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing()).stream()
				.toList();
	}

	@Cacheable("findAllActual")
	public List<Profile> findAllActual(String actual, int idReport) {
		ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
		return this.counterSummaryService.recoverCounterSummary(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing(), actual).stream()
				.toList();
	}

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

	@Override
	@Cacheable("findAllProfileTotals")
	public List<ProfileTotal> findAllProfileTotals(String id, int idReport) {

		List<Profile> listAll = new ArrayList<Profile>();
		List<Profile> listActual = new ArrayList<Profile>();
		List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype(id, "r");
		switch (id) {
		case "Engagement Managers":
			listActual = findAllActual(id, idReport);
			return engagementManagersTotal(findByTypeAndSubtype, listActual);
		case "Architects":
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
			throw new MyBadAdviceException("entrada no válida");
		}
	}

	private List<ProfileTotal> engagementManagersTotal(List<Literal> findByTypeAndSubtype, List<Profile> list) {

		List<Profile> listEM = list.stream().filter(p->p.getPerfil().contains("Engagement Managers")).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listEM.size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("SAFE")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM's Certification Level 1")).toList().size()));
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
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getCertificaciones().contains("EM's Certification Level 1")).toList().size()));
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

	@Override
	public List<ProfileGroup> findAllProfile(String id, int idReport) {

		List<Profile> listAll = this.findAll(idReport);
		List<Profile> listActual = findAllActual(id, idReport);
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
