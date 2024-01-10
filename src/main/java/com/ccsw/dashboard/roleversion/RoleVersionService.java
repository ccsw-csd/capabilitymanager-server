package com.ccsw.dashboard.roleversion;



import java.util.List;

import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;

public interface RoleVersionService {

	List<RoleVersion> findAll();
	RoleVersion findById(Long id);
	List<String> findYears();
	
	void save(Long id, RoleVersionDto dto);
	
}
