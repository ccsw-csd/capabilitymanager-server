package com.ccsw.dashboard.views.repository;

import java.util.Collection;

import com.ccsw.dashboard.profile.model.Profile;

public interface ViewCounterSummaryRepository {

	public Collection<Profile> generateConutersSummaryByRole(int idVersionCapacidades, int idVersionStaffing, String profileId);
	
	public Collection<Profile> generateConutersSummaryByAll(int idVersionCapacidades, int idVersionStaffing);
}
