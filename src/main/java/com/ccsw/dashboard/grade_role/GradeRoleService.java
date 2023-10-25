package com.ccsw.dashboard.grade_role;



import java.util.List;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.grade_role.model.GradeRole;
import com.ccsw.dashboard.grade_role.model.GradeRoleTotal;

public interface GradeRoleService {

	List<GradeRole> findAll();
	List<GradeRoleTotal> findAlll();
	List<Grade> getGrades();
	List<Role> getRoles();
	
}
