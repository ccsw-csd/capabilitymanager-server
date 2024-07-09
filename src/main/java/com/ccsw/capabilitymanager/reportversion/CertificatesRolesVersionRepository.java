package com.ccsw.capabilitymanager.reportversion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.reportversion.model.CertificatesRolesVersion;

@Repository
public interface CertificatesRolesVersionRepository extends JpaRepository<CertificatesRolesVersion, Long> {

}
