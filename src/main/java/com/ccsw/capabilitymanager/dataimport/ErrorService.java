package com.ccsw.capabilitymanager.dataimport;

import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

public interface ErrorService {
	void staffingError(VersionStaffing verStaf);
	void formError(VersionCapacidades verCap);
}
