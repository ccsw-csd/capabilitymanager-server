package com.ccsw.dashboard.views;

import java.util.Collection;

import com.ccsw.dashboard.graderole.model.GradeRole;

public interface ViewGradosRolesRepository {

	public Collection<GradeRole> findAll(int idVersionCapacidades, int idVersionStaffing);
}
