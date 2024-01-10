package com.ccsw.dashboard.RoleVersion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.RoleVersion.model.RoleVersion;
import com.ccsw.dashboard.RoleVersion.model.RoleVersionDto;

@RequestMapping(value = "/roleimports")
@RestController
public class RoleVersionController {

    @Autowired
    private RoleVersionService roleVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<RoleVersion> findAll(){        
        return this.roleVersionService.findAll(); 
    }
    
    @RequestMapping(path = "/all/{year}", method = RequestMethod.GET)
    public List<RoleVersionDto> findAllYear(@PathVariable String year){       
    	return this.roleVersionService.findAll().stream().filter(rv->String.valueOf(rv.getFechaImportacion().getYear()).equals(year)).map(rv->mapper.map(rv, RoleVersionDto.class)).toList();
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public RoleVersion findById(@PathVariable String id){
        return this.roleVersionService.findById(Long.valueOf(id));     
    }
    
    @RequestMapping(path = "/years", method = RequestMethod.GET)
    public List<String> findYears(){
       return this.roleVersionService.findYears();
    }
    
    @RequestMapping(path = { "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody RoleVersionDto dto) {
        this.roleVersionService.save(id, dto);
    }
}
