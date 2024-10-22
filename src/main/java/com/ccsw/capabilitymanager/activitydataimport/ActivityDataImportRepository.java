package com.ccsw.capabilitymanager.activitydataimport;

import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ActivityDataImportRepository extends JpaRepository<ActivityDataImport, Long> {

	@Query("SELECT a FROM ActivityDataImport a WHERE a.gGID = :gGID AND a.pathwayTitle = :pathwayTitle")
	List<ActivityDataImport> findIgual(@Param("gGID") String gGID, @Param("pathwayTitle") String pathwayTitle);


}