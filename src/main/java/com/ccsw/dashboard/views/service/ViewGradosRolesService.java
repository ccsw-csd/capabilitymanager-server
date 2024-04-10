package com.ccsw.dashboard.views.service;

import java.util.Collection;

import com.ccsw.dashboard.graderole.model.GradeRole;

public interface ViewGradosRolesService {

	public Collection<GradeRole> getAll(int idCapacidades,int idStaffing);
}
