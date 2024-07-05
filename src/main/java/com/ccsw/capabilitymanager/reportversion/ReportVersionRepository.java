package com.ccsw.capabilitymanager.reportversion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;

@Repository
public interface ReportVersionRepository extends JpaRepository<ReportVersion, Long> {

	Optional<ReportVersion> findById(Long id);
	
	Optional<ReportVersion> findByIdVersionCapacidades(Long id);	
	List<ReportVersion> findByScreenshot(int id);
	List<ReportVersion> findByFechaImportacionBetween(LocalDateTime to, LocalDateTime from);
	List<ReportVersion> findByScreenshotAndFechaImportacionBetween(int id, LocalDateTime from, LocalDateTime to);


}
