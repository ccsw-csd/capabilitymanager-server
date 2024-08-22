package com.ccsw.capabilitymanager.dataimport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.versioncapacidades.VersionCapatidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ErrorServiceImpl implements ErrorService {

	@Autowired
	private VersionCapatidadesRepository versionCapatidadesRepository;
	
	@Autowired
	private VersionStaffingRepository versionStaffingRepository;

	/**
	 * Saves the given {@link VersionStaffing} object to the repository.
	 *
	 * <p>This method persists the provided {@link VersionStaffing} instance to the {@link versionStaffingRepository}.
	 * It is used to record an error or log an instance of {@link VersionStaffing} that needs to be stored in the repository.</p>
	 *
	 * @param verStaf The {@link VersionStaffing} object to be saved in the repository.
	 */
	@Override
	public void staffingError(VersionStaffing verStaf) {
		versionStaffingRepository.save(verStaf);
		
	}

	/**
	 * Saves the given {@link VersionCapacidades} object to the repository.
	 *
	 * <p>This method persists the provided {@link VersionCapacidades} instance to the {@link versionCapacidadesRepository}.
	 * It is used to record an error or log an instance of {@link VersionCapacidades} that needs to be stored in the repository.</p>
	 *
	 * @param verCap The {@link VersionCapacidades} object to be saved in the repository.
	 */
	@Override
	public void formError(VersionCapacidades verCap) {
		versionCapatidadesRepository.save(verCap);
		
	}

	
}
