package com.ccsw.dashboard.views.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.ccsw.dashboard.profile.model.Profile;
import com.ccsw.dashboard.views.repository.ViewCounterSummaryRepository;
import com.ccsw.dashboard.views.service.CounterSummaryService;

@Service
public class CounterSummaryServiceImpl implements CounterSummaryService{
	
	private final ViewCounterSummaryRepository repository;
	
	
	public CounterSummaryServiceImpl(ViewCounterSummaryRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Collection<Profile> recoverCounterSummary(int idVersionCapacidades, int idVersionStaffing, String profileId) {
		return repository.generateConutersSummaryByRole(idVersionCapacidades, idVersionStaffing, profileId);
	}

	@Override
	public Collection<Profile> recoverCounterSummaryAll(int idVersionCapacidades, int idVersionStaffing) {
		return repository.generateConutersSummaryByAll(idVersionCapacidades, idVersionStaffing);
	}		
	
}
