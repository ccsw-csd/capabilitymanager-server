package com.ccsw.dashboard.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.literal.LiteralService;
import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.exception.MyBadAdviceException;
import com.ccsw.dashboard.graderole.GradeRoleService;
import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.graderole.model.GradeTotal;
import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileGroup;
import com.ccsw.dashboard.profile.model.ProfileTotal;
import com.ccsw.dashboard.profile.model.InformeRoles;
import com.ccsw.dashboard.reportversion.ReportVersionService;
import com.ccsw.dashboard.reportversion.model.ReportVersion;
import com.ccsw.dashboard.views.CounterSummaryService;
import com.ccsw.dashboard.views.ViewGradosRolesService;

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
    	return (List<Profile>) this.counterSummaryService.recoverCounterSummaryAll(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing()).stream()
        		.toList();
    }
    
    @Cacheable("findAllActual")
    public List<Profile> findAllActual(String actual, int idReport) {
    	ReportVersion rv = reportVersionService.findById(Long.valueOf(idReport));
    	return (List<Profile>) this.counterSummaryService.recoverCounterSummary(rv.getIdVersionCapacidades(), rv.getIdVersionStaffing(), actual).stream()
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
		
		architects = findAllProfileTotals("Architects", idReport);
		softwareEngineer = findAllProfileTotals("Software Engineer", idReport);
		industryExperts = findAllProfileTotals("Industry Experts", idReport);
		engagementManagers = findAllProfileTotals("Engagement Managers", idReport);
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
		ProfileTotal profileTotal = new ProfileTotal();
		profileTotal.setProfile(findByTypeAndSubtype.get(0).getDesc());
		profileTotal.setTotals(totals);
		
		List<ProfileTotal> profileTotalList = new ArrayList<>();
		profileTotalList.add(profileTotal);
		profileTotal = new ProfileTotal();
		totals = new ArrayList<Long>();
		listEM = list.stream().filter(p->p.getPerfil().contains("PMO")).toList();
		totals.add(Long.valueOf(listEM.size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Experiencia en soluciones complejas")).toList().size()));
		totals.add(Long.valueOf(listEM.stream().filter(p->p.getExperiencia().contains("Agile")).toList().size()));
		listEM = list.stream().filter(p->p.getPerfil().contains("Scrum Master")).toList();
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
		List<Profile> listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Consumer Goods & Retail")).toList();
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
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Telco, Media & Technologies")).toList();
		sum=sum+Long.valueOf(listIndustryExperts.size());
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Financial Services")).toList();
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
		String actual = i==0?"Architects":"Software Engineer";
		String perfil = i==0?"Solution":"SE";
		List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Backend JAVA")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Backend .NET")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Full stack JAVA")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Full stack .NET")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("FrontEnd")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("MainFrame")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("DevOps Automation")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Otro")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().contains("AWS")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().contains("Azure")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().contains("GCP")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillCloudNative().contains("Other Clouds")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().contains("Outsystems")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().contains("PowerApps")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().contains("Mendix")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getSkillLowCode().contains("Other")).toList();
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
		String actual = i==0?"Architects":"Software Engineer";
		String perfil = i==0?"Integration":"SE";
		List<Profile> listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Mulesoft")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Boomi")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Software AG")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Tibco")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Other vendor")).toList(); 
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getActual().equals(actual)).filter(p->p.getPerfil().contains(perfil)).filter(p->p.getTecnico().contains("Open Source")).toList();
		totals.add(Long.valueOf(listArchitects.size()));			
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
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Consumer Goods & Retail")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Energy & Utilities")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Manufacturing")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Life Science")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Public Sector")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Telco, Media & Technologies")).toList());
		listIndustryExperts.addAll(list.stream().filter(p->p.getSectorExperiencia().contains("Financial Services")).toList());
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
