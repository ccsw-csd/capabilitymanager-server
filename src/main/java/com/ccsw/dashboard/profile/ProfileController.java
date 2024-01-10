package com.ccsw.dashboard.profile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileTotal;

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

    @RequestMapping(path = "/db", method = RequestMethod.GET)
    public Map<String, Map<String, Long>> findAllDb(@RequestParam(value = "idImport", required = true) int idImport) {
        return this.profileService.findAll(idImport).stream().collect(Collectors.groupingBy(Profile::getActual, Collectors.groupingBy(Profile::getPerfil, Collectors.counting())));
    }
    
    @RequestMapping(path = "/profiletotals/{id}", method = RequestMethod.GET)
    public List<ProfileTotal> findAllProfileTotals(@PathVariable String id, 
    		@RequestParam(value = "idImport", required = true) int idImport) {    	 	
    	return this.profileService.findAllProfileTotals(id, idImport);
    }
    

    @RequestMapping(path = "/profiletotals/{id}/csv", method = RequestMethod.GET)
    public void findAllProfileTotalsCsv(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idImport", required = true) int idImport) throws IOException {
    	exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idImport));
    	exportService.writeProfileTotalsToCsv(id, servletResponse);
    }

    
    @RequestMapping(path = "/profiletotals/{id}/excel", method = RequestMethod.GET)
    public void findAllProfileTotalsExcel(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idImport", required = true) int idImport) throws IOException {
    	exportService.setProfileTotals(this.profileService.findAllProfileTotals(id, idImport));
    	exportService.writeProfileTotalsToExcel(id, servletResponse);
    }
    
    @RequestMapping(path = "/profilelist/{id}/excel", method = RequestMethod.GET)
    public void findAllProfileExcel(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idImport", required = true) int idImport) throws IOException {
    	exportService.setProfileGroup(this.profileService.findAllProfile(id, idImport));
    	exportService.writeProfileToExcel(id, servletResponse);
    }
    
    @RequestMapping(path = "/profilelist/{id}/xls", method = RequestMethod.GET)
    public void findAllProfileXls(HttpServletResponse servletResponse, @PathVariable String id, 
    		@RequestParam(value = "idImport", required = true) int idImport)  throws IOException {
    	exportService.setProfileGroup(this.profileService.findAllProfile(id, idImport));
    	exportService.writeProfileToTemplateExcel(id, servletResponse);
    }
    
}
