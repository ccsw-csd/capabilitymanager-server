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
	public List<ProfileTotal> findAllExperienceTotals() {	
    	List<Profile> list = this.profileRepository.findAll();    	
     	return industryExperts(list);	
    }
	@Override
	public List<ProfileTotal> findAllProfileTotals(String id) {		
						
		List<Profile> list = this.profileRepository.findAll().stream().filter(p->p.getActual().equals(id)).toList();	
		List<Literal> findByTypeAndSubtype = literalService.findByTypeAndSubtype(id, "r");
		switch (id) {
		  case "Engagement Managers":
			  return engagementManagers(findByTypeAndSubtype, list);	
		  case "Architects":
			  return Architects(findByTypeAndSubtype, list);
		  case "Business Analyst":
		      return businessAnalyst(findByTypeAndSubtype, list);
		  case "Software Engineer":			 
			  return softwareEngineer(findByTypeAndSubtype, list);		  
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
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Energy & Utilities")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Manufacturing")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Life Science")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Public Sector")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Telco, Media & Technologies")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		listIndustryExperts = list.stream().filter(p->p.getSectorExperiencia().contains("Financial Services")).toList();
		totals.add(Long.valueOf(listIndustryExperts.size()));
		ProfileTotal profileTotal = new ProfileTotal();
		profileTotal.setProfile(literal.getDesc());
		profileTotal.setTotals(totals);			
		profileTotalList.add(profileTotal);			
	}	
	return profileTotalList;
}
	
}
