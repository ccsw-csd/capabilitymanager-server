package com.ccsw.dashboard.views.service;

import java.util.Collection;

import com.ccsw.dashboard.profile.model.Profile;

public interface CounterSummaryService {

	public Collection<Profile> recoverCounterSummary(int idVersionCapacidades, int idVersionStaffing, String profileId);
	
	public Collection<Profile> recoverCounterSummaryAll(int idVersionCapacidades, int idVersionStaffing);
}
