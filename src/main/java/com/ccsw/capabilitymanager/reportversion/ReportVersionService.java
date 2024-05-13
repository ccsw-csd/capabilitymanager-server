package com.ccsw.capabilitymanager.reportversion;



import java.util.List;

import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;

public interface ReportVersionService {

	List<ReportVersion> findAll();
	ReportVersion findById(Long id);
	ReportVersion findByIdVersionCapacidades(Long id);
	List<String> findYears(String screenshot);
	
	void save(Long id, ReportVersionDto dto);
	List<ReportVersion> findByScreenshot(String id, String year);
	
	ReportVersion generateReport(GenerateReportVersionDto dto);
	
}
