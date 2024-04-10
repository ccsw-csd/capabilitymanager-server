package com.ccsw.dashboard.certificatesversion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccsw.dashboard.certificatesversion.model.CertificatesVersion;

public interface CertificatesVersionRepository extends JpaRepository<CertificatesVersion, Long> {

	Optional<CertificatesVersion> findById(Long id);

}
