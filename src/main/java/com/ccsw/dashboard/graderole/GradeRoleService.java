package com.ccsw.dashboard.graderole;



import java.util.Collection;
import java.util.List;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.graderole.model.GradeRoleTotal;
import com.ccsw.dashboard.graderole.model.GradeTotal;

public interface GradeRoleService {

	Collection<GradeRole> findAll(int idImport);
	List<GradeRoleTotal> findAlll(int idImport);
	List<Literal> getLiteralGrades();
	List<Literal> getLiteralRoles();
	List<GradeTotal> findAllGradeTotals(int idImport);
	List<Grade> getGrades();
	List<Role> getRoles();
	
}
