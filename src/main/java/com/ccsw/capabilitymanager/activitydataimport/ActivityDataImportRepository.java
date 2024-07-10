package com.ccsw.capabilitymanager.activitydataimport;

import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ActivityDataImportRepository extends JpaRepository<ActivityDataImport, Long> {


}