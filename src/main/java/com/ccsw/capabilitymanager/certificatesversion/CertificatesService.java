package com.ccsw.capabilitymanager.certificatesversion;

import java.util.List;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;

public interface CertificatesService {

	List<CertificatesVersion> findAll();

	CertificatesVersion findById(Long id);

	List<String> findYears();

	void save(Long id, CertificatesVersionDto dto);

}
