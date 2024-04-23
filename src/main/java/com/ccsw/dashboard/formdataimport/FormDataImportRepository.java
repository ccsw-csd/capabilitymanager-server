package com.ccsw.dashboard.formdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.dashboard.formdataimport.model.FormDataImport;

@Repository
@Transactional
public interface FormDataImportRepository extends JpaRepository<FormDataImport, Long> {

}
