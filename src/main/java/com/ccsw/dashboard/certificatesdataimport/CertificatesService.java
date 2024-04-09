package com.ccsw.dashboard.certificatesdataimport;

import java.util.List;

import com.ccsw.dashboard.certificatesdataimport.model.CertificatesVersion;
import com.ccsw.dashboard.certificatesdataimport.model.CertificatesVersionDto;

public interface CertificatesService {

	List<CertificatesVersion> findAll();

	CertificatesVersion findById(Long id);

	List<String> findYears();

	void save(Long id, CertificatesVersionDto dto);

}
