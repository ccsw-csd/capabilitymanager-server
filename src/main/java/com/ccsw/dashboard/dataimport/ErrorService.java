package com.ccsw.dashboard.dataimport;

import com.ccsw.dashboard.versioncapacidades.model.VersionCapacidades;
import com.ccsw.dashboard.versionstaffing.model.VersionStaffing;

public interface ErrorService {
	void staffingError(VersionStaffing verStaf);
	void formError(VersionCapacidades verCap);
}
