package com.ccsw.dashboard.certificatesdataimport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.dashboard.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.dashboard.certificatesdataimport.model.CertificatesVersion;

@Repository
@Transactional
public interface CertificatesDataImportRepository extends JpaRepository<CertificatesVersion, Long> {
	List<CertificatesVersion> findAll();

	List<CertificatesDataImport> save(List<CertificatesDataImport> certificatesDataImportList);
}
