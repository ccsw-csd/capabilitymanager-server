package com.ccsw.capabilitymanager.staffingversion;



import java.util.List;

import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

public interface StaffingVersionService {

	List<StaffingVersion> findAll();
	StaffingVersion findById(Long id);
	List<String> findYears();
	
	void save(Long id, StaffingVersionDto dto);
	
}
