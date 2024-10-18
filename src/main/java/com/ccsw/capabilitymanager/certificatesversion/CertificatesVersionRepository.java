package com.ccsw.capabilitymanager.certificatesversion;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface CertificatesVersionRepository extends JpaRepository<CertificatesVersion, Long> {

	Optional<CertificatesVersion> findById(Long id);

}
