package com.ccsw.capabilitymanager.certificatesdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesActividadDataImport;

@Repository
@Transactional
public interface CertificatesActividadDataImportRepository extends JpaRepository<CertificatesActividadDataImport, Long> {


}
