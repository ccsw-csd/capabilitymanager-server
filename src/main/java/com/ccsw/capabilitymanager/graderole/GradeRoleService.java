package com.ccsw.capabilitymanager.graderole;



import java.util.Collection;
import java.util.List;

import com.ccsw.capabilitymanager.config.grade.model.Grade;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.config.role.model.Role;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotal;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;

public interface GradeRoleService {

	Collection<GradeRole> findAll(int idImport);
	List<GradeRoleTotal> findAlll(int idImport);
	List<Literal> getLiteralGrades();
	List<Literal> getLiteralRoles();
	List<GradeTotal> findAllGradeTotals(int idImport);
	List<Grade> getGrades();
	List<Role> getRoles();
	
}
