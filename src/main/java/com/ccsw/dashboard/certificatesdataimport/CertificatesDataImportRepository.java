package com.ccsw.dashboard.certificatesdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.dashboard.certificatesdataimport.model.CertificatesDataImport;

@Repository
@Transactional
public interface CertificatesDataImportRepository extends JpaRepository<CertificatesDataImport, Long> {
}
