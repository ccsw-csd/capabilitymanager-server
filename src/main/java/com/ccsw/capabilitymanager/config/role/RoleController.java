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
    
    /**
     * Handles HTTP GET requests to retrieve a list of all roles.
     *
     * @return A list of {@link RoleDto} objects representing all roles.
     */
    @GetMapping("/config")
    public List<RoleDto> findAll(){
        return this.roleService.findAll().stream().map(g->mapper.map(g,RoleDto.class)).toList();
    }  
    
    /**
     * Handles HTTP GET requests to retrieve a role by its ID.
     *
     * @param id The ID of the role to retrieve. This ID is provided as a {@link String} and is converted to a {@code Long}.
     * @return The {@link Role} object with the specified ID, or {@code null} if no role with the given ID exists.
     * @throws NumberFormatException If the ID cannot be converted to a {@code Long}.
     */  
    @GetMapping("/{id}")
    public Role findById(@PathVariable String id){
        return this.roleService.findById(Long.valueOf(id)); 
    }
}
