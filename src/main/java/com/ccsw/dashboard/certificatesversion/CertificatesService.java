package com.ccsw.dashboard.certificatesversion;

import java.util.List;

import com.ccsw.dashboard.certificatesversion.model.CertificatesVersion;
import com.ccsw.dashboard.certificatesversion.model.CertificatesVersionDto;

public interface CertificatesService {

	List<CertificatesVersion> findAll();

	CertificatesVersion findById(Long id);

	List<String> findYears();

	void save(Long id, CertificatesVersionDto dto);

}
