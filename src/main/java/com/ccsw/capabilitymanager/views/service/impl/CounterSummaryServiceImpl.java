package com.ccsw.capabilitymanager.views.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.views.repository.ViewCounterSummaryRepository;
import com.ccsw.capabilitymanager.views.service.CounterSummaryService;

@Service
public class CounterSummaryServiceImpl implements CounterSummaryService{
	
	private final ViewCounterSummaryRepository repository;
	
	
	public CounterSummaryServiceImpl(ViewCounterSummaryRepository repository) {
		super();
		this.repository = repository;
	}

	/**
	 * Retrieves a collection of Profile objects that provide a summary of counters based on the specified version IDs and profile ID.
	 * 
	 * @param idVersionCapacidades the ID of the version from the capacidades table.
	 * @param idVersionStaffing the ID of the version from the staffing table.
	 * @param profileId the ID of the profile for which the summary is to be retrieved.
	 * @return a collection of Profile objects summarizing the counters for the specified profile.
	 */
	@Override
	public Collection<Profile> recoverCounterSummary(int idVersionCapacidades, int idVersionStaffing, String profileId) {
		return repository.generateConutersSummaryByRole(idVersionCapacidades, idVersionStaffing, profileId);
	}

	/**
	 * Retrieves a collection of Profile objects that provide a summary of counters for all profiles based on the specified version IDs.
	 * This method queries the repository to generate a summary that includes data for all profiles associated with the given version IDs.
	 * 
	 * @param idVersionCapacidades the ID of the version from the capacidades table.
	 * @param idVersionStaffing the ID of the version from the staffing table.
	 * @return a collection of Profile objects summarizing the counters for all profiles related to the specified version IDs.
	 */
	@Override
	public Collection<Profile> recoverCounterSummaryAll(int idVersionCapacidades, int idVersionStaffing) {
		return repository.generateConutersSummaryByAll(idVersionCapacidades, idVersionStaffing);
	}

	/**
	 * Retrieves the version of certifications based on the specified version ID.
	 * This method invokes the repository to obtain certification details associated with the given version ID.
	 * 
	 * @param idVersionCertificaciones the ID of the version of certifications to retrieve.
	 * @throws SomeCustomException if an error occurs during the retrieval process.
	 */
	@Override
	public void obtenerVersionCertificaciones(int idVersionCertificaciones) {
		 repository.obtenerVersionCertificaciones(idVersionCertificaciones);
	}		
	
}
