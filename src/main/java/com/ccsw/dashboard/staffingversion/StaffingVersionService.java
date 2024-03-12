package com.ccsw.dashboard.staffingversion;



import java.util.List;

import com.ccsw.dashboard.staffingversion.model.StaffingVersion;
import com.ccsw.dashboard.staffingversion.model.StaffingVersionDto;

public interface StaffingVersionService {

	List<StaffingVersion> findAll();
	StaffingVersion findById(Long id);
	List<String> findYears();
	
	void save(Long id, StaffingVersionDto dto);
	
}
