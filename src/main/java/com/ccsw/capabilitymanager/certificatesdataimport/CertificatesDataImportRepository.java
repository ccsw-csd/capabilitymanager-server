package com.ccsw.capabilitymanager.certificatesdataimport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;



@Repository
@Transactional
public interface CertificatesDataImportRepository extends JpaRepository<CertificatesDataImport, Long> {

	 @Query("SELECT c FROM CertificatesDataImport c WHERE c.numImportCodeId = :numImportCodeId")
	    List<CertificatesDataImport> findByNumImportCodeId(@Param("numImportCodeId") Long numImportCodeId);
}
