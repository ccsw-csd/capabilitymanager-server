package com.ccsw.dashboard.formdataimport;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.formdataimport.model.FormDataImport;



@Repository
public interface FormDataImportRepository extends JpaRepository<FormDataImport, Long> {
}
