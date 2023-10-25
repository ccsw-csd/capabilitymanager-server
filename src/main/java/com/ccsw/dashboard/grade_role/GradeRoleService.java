package com.ccsw.dashboard.grade_role;



import java.util.List;

import com.ccsw.dashboard.grade_role.model.GradeRole;
import com.ccsw.dashboard.grade_role.model.GradeRoleAll;

public interface GradeRoleService {

	List<GradeRole> findAll();
	GradeRoleAll findAlll();
	
}
