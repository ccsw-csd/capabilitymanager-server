package com.ccsw.capabilitymanager.views.repository;

import java.util.Collection;

import com.ccsw.capabilitymanager.profile.model.Profile;

public interface ViewCounterSummaryRepository {

	public Collection<Profile> generateConutersSummaryByRole(int idVersionCapacidades, int idVersionStaffing, String profileId);
	
	public Collection<Profile> generateConutersSummaryByAll(int idVersionCapacidades, int idVersionStaffing);
	
	public void obtenerVersionCertificaciones(int idVersionCertificaciones);
}
