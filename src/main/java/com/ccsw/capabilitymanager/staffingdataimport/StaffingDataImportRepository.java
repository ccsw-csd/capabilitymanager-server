package com.ccsw.capabilitymanager.staffingdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.staffingdataimport.model.StaffingDataImport;

@Repository
@Transactional
public interface StaffingDataImportRepository extends JpaRepository<StaffingDataImport, Long> {
}
