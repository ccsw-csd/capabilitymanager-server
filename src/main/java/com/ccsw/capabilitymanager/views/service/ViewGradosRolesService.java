package com.ccsw.capabilitymanager.views.service;

import java.util.Collection;

import com.ccsw.capabilitymanager.graderole.model.GradeRole;

public interface ViewGradosRolesService {

	public Collection<GradeRole> getAll(int idCapacidades,int idStaffing);
}
