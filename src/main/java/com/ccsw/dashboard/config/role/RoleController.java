package com.ccsw.dashboard.config.role;


import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.config.role.model.RoleDto;

import java.util.List;


@RequestMapping(value = "/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<RoleDto> findAll(){
        return this.roleService.findAll().stream().map(g->mapper.map(g,RoleDto.class)).toList();
    }  
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Role findById(@PathVariable String id){
        return this.roleService.findById(Long.valueOf(id)); 
    }
}
