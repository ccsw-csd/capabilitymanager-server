package com.ccsw.capabilitymanager.roleversion;



import java.util.List;

import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;

public interface RoleVersionService {

	List<RoleVersion> findAll();
	RoleVersion findById(Long id);
	List<String> findYears();
	
	void save(Long id, RoleVersionDto dto);
	
}
