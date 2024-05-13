package com.ccsw.capabilitymanager.views.repository;

import java.util.Collection;

import com.ccsw.capabilitymanager.graderole.model.GradeRole;

public interface ViewGradosRolesRepository {

	public Collection<GradeRole> findAll(int idVersionCapacidades, int idVersionStaffing);
}
