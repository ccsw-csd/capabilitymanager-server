package com.ccsw.dashboard.graderole;



import java.util.List;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.graderole.model.GradeRoleTotal;
import com.ccsw.dashboard.graderole.model.GradeTotal;

public interface GradeRoleService {

	List<GradeRole> findAll();
	List<GradeRoleTotal> findAlll();
	List<Grade> getGrades();
	List<Role> getRoles();
	List<GradeTotal> findAllGradeTotals();
	
}
