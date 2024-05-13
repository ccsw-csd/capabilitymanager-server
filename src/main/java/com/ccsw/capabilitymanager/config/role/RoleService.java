package com.ccsw.capabilitymanager.config.role;

import com.ccsw.capabilitymanager.config.role.model.Role;

import java.util.List;

public interface RoleService {

	List<Role> findAll();
	Role findByRole(String Role);
	Role findById(Long valueOf);   

}
