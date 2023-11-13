package com.ccsw.dashboard.profile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.profile.model.ProfileTotal;

@RequestMapping(value = "/profile")
@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/db", method = RequestMethod.GET)
    public Map<String, Map<String, Long>> findAllDb(){
        return this.profileService.findAll().stream().collect(Collectors.groupingBy(Profile::getActual, Collectors.groupingBy(Profile::getPerfil, Collectors.counting())));
    }
    
    @RequestMapping(path = "/profiletotals/{id}", method = RequestMethod.GET)
    public List<ProfileTotal> findAllProfileTotals(@PathVariable String id){    	 	
    	return this.profileService.findAllProfileTotals(id);
    }
    
}
