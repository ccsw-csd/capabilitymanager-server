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
	
	@Override
	public void staffingError(VersionStaffing verStaf) {
		versionStaffingRepository.save(verStaf);
		
	}

	@Override
	public void formError(VersionCapacidades verCap) {
		versionCapatidadesRepository.save(verCap);
		
	}

	
}
