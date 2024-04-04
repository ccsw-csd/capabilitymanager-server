package com.ccsw.dashboard.views.repository;

import java.util.Collection;

import com.ccsw.dashboard.dataformation.model.DataFormation;

public interface ViewListadoBenchRepository {
	public Collection<DataFormation> getDataFormation(int idVersionCapacidades, int idVersionStaffing,int idCertidficacion);

}
