package com.ccsw.dashboard.profile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.literal.LiteralService;
import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileTotal;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private LiteralService literalService;    
    
    @Override
    public List<Profile> findAll() {
        return (List<Profile>) this.profileRepository.findAll();
    }    
    
	@Override
	public List<ProfileTotal> findAllProfileTotals(String id) {		
						
		List<Profile> list = this.profileRepository.findAll();
		List<Profile> listId = list.stream().filter(p->p.getActual().equals(id)).toList();
		List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype(id, "r");
		switch (id) {
		  case "Engagement Managers":
			  return engagementManagers(findByTypeAndSubtype, listId);	
		  case "Architects":
			  return Architects(findByTypeAndSubtype, listId);
		  case "Business Analyst":
		      return businessAnalyst(findByTypeAndSubtype, listId);
		  case "Software Engineer":			 
			  return softwareEngineer(findByTypeAndSubtype, listId);
		  case "Industry Experts":			     	
		      return industryExperts(list);	
		  default:
			  System.out.println("entrada no válida");
			  //TODO lanzar exception
		}
		return null;		
	}

	private List<ProfileTotal> engagementManagers(List<Literal> findByTypeAndSubtype, List<Profile> list) {
						
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
	
	private List<ProfileTotal> Architects(List<Literal> findByTypeAndSubtype, List<Profile> list) {
		
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
	
	private List<ProfileTotal> businessAnalyst(List<Literal> findByTypeAndSubtype, List<Profile> list) {
		
		List<ProfileTotal> profileTotalList = new ArrayList<>();		
		for (Literal literal : findByTypeAndSubtype) {
//			List<Profile> listBusinessAnalyst = list.stream().filter(p->p.getPerfil().contains(literal.getDesc())).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(list.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);			
			profileTotalList.add(profileTotal);			
		}		
		
		return profileTotalList;
	}
	
private List<ProfileTotal> softwareEngineer(List<Literal> findByTypeAndSubtype, List<Profile> list) {
		
		List<ProfileTotal> profileTotalList = new ArrayList<>();		
		for (Literal literal : findByTypeAndSubtype) {
//			List<Profile> listSoftwareEngineer = list.stream().filter(p->p.getPerfil().contains(literal.getDesc())).toList();
			ArrayList<Long> totals = new ArrayList<Long>();
			totals.add(Long.valueOf(list.size()));
			ProfileTotal profileTotal = new ProfileTotal();
			profileTotal.setProfile(literal.getDesc());
			profileTotal.setTotals(totals);			
			profileTotalList.add(profileTotal);			
		}		
		
		return profileTotalList;
	}

private List<ProfileTotal> industryExperts(List<Profile> list) {	
	
	List<ProfileTotal> profileTotalList = new ArrayList<>();
	List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype("Industry Experts", "r");
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

private List<ProfileTotal> ArchitectsAndSECustomAppsDevelopment(List<Profile> list) {	
	
	List<ProfileTotal> profileTotalList = new ArrayList<>();
	List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype("Architects & SE Custom Apps Development", "r");
	for (Literal literal : findByTypeAndSubtype) {		
		List<Profile> listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Back end Java")).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Back end .Net")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Full Stack Java")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Full Stack .Net")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Front End")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("SW Dev Mainframe")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("DevOps Automation")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Other")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("AWS")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Azure")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("GCP")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Other clouds")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Outsystems")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("PowerApps")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Mendix")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Others")).toList();
		totals.add(Long.valueOf(listArchitects.size()));		
		ProfileTotal profileTotal = new ProfileTotal();
		profileTotal.setProfile(literal.getDesc());
		profileTotal.setTotals(totals);			
		profileTotalList.add(profileTotal);			
	}	
	return profileTotalList;
}
	
private List<ProfileTotal> ArchitectsAndSEIntegrationAndAPIs(List<Profile> list) {	
	
	List<ProfileTotal> profileTotalList = new ArrayList<>();
	List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype("Architects & SE Integration & APIs", "r");
	for (Literal literal : findByTypeAndSubtype) {		
		List<Profile> listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Mulesoft")).toList();
		ArrayList<Long> totals = new ArrayList<Long>();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Boomi")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Software AG")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Tibco")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Other vendor")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		listArchitects = list.stream().filter(p->p.getSectorExperiencia().contains("Open Source")).toList();
		totals.add(Long.valueOf(listArchitects.size()));
		ProfileTotal profileTotal = new ProfileTotal();
		profileTotal.setProfile(literal.getDesc());
		profileTotal.setTotals(totals);			
		profileTotalList.add(profileTotal);			
	}	
	return profileTotalList;
}
	
}
