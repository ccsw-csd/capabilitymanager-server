package com.ccsw.dashboard.profile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileTotal;
import com.ccsw.dashboard.profile.model.InformeRoles;

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

    @GetMapping("/db")
    public Map<String, Map<String, Long>> findAllDb(@RequestParam(value = "idReport", required = true) int idReport) {
        return this.profileService.findAll(idReport).stream().collect(Collectors.groupingBy(Profile::getActual, Collectors.groupingBy(Profile::getPerfil, Collectors.counting())));
    }
    
    @GetMapping("/profiletotals/{id}")
    public List<ProfileTotal> findAllProfileTotals(@PathVariable String id, 
    		@RequestParam(value = "idReport", required = true) int idReport) {    	 	
    	return this.profileService.findAllProfileTotals(id, idReport);
    }
    
    @GetMapping("/informeRoles")
    public InformeRoles findAllInformeRoles(@RequestParam(value = "idReport", required = true) int idReport) {    	 	
    	return this.profileService.findAllInformeRoles(idReport);
    }
    

    @RequestMapping(path = "/profiletotals/{id}/csv", method = RequestMethod.GET)
    public void findAllProfileTotalsCsv(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
    	exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idReport));
    	exportService.writeProfileTotalsToCsv(id, servletResponse);
    }

    
    @RequestMapping(path = "/profiletotals/{id}/excel", method = RequestMethod.GET)
    public void findAllProfileTotalsExcel(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
    	exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idReport));
    	exportService.writeProfileTotalsToExcel(id, servletResponse);
    }
    
    @RequestMapping(path = "/profilelist/{id}/excel", method = RequestMethod.GET)
    public void findAllProfileExcel(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idReport", required = true) int idReport) throws IOException {
    	exportService.setProfileGroup(this.profileService.findAllProfile(id, idReport));
    	exportService.writeProfileToExcel(id, servletResponse);
    }
    
    @RequestMapping(path = "/profilelist/{id}/xls", method = RequestMethod.GET)
    public void findAllProfileXls(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idReport", required = true) int idReport)  throws IOException {
    	exportService.setProfileGroup(this.profileService.findAllProfile(id, idReport));
    	exportService.writeProfileToTemplateExcel(id, servletResponse);
    }
    
}
