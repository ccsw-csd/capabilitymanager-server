package com.ccsw.capabilitymanager.config.role;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccsw.capabilitymanager.config.role.model.Role;
import com.ccsw.capabilitymanager.config.role.model.RoleDto;


@RequestMapping(value = "/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/config")
    public List<RoleDto> findAll(){
        return this.roleService.findAll().stream().map(g->mapper.map(g,RoleDto.class)).toList();
    }  
    
    @GetMapping("/{id}")
    public Role findById(@PathVariable String id){
        return this.roleService.findById(Long.valueOf(id)); 
    }
}
