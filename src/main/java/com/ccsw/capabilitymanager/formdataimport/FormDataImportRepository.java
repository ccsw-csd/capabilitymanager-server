package com.ccsw.capabilitymanager.formdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;

@Repository
@Transactional
public interface FormDataImportRepository extends JpaRepository<FormDataImport, Long> {

}
