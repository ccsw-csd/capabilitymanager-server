package com.ccsw.dashboard.views.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.views.repository.ViewGradosRolesRepository;
import com.ccsw.dashboard.views.service.ViewGradosRolesService;

@Service
public class ViewGradosRolesServiceImpl implements ViewGradosRolesService {
	
	private final ViewGradosRolesRepository repository;

	public ViewGradosRolesServiceImpl(ViewGradosRolesRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Collection<GradeRole> getAll(int idCapacidades, int idStaffing) {
		return repository.findAll(idCapacidades, idStaffing);
	}

}
