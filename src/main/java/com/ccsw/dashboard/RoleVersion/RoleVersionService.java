package com.ccsw.dashboard.RoleVersion;



import java.util.List;

import com.ccsw.dashboard.RoleVersion.model.RoleVersion;
import com.ccsw.dashboard.RoleVersion.model.RoleVersionDto;

public interface RoleVersionService {

	List<RoleVersion> findAll();
	RoleVersion findById(Long id);
	List<String> findYears();
	
	void save(Long id, RoleVersionDto dto);
	
}
