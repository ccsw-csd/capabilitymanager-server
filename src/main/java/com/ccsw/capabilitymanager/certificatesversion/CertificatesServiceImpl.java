package com.ccsw.capabilitymanager.certificatesversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CertificatesServiceImpl implements CertificatesService {

	@Autowired
	private CertificatesVersionRepository certificatesVersionRepository;

	@Override
	public List<CertificatesVersion> findAll() {
		return (List<CertificatesVersion>) this.certificatesVersionRepository.findAll().stream().sorted().toList();
	}

	@Override
	public CertificatesVersion findById(Long id) {
		return this.certificatesVersionRepository.findById(id).orElse(null);
	}

	@Override
	public List<String> findYears() {
		List<String> cvList = new ArrayList<String>();
		Map<String, String> cvMap = new HashMap<String, String>();
		List<CertificatesVersion> listCertificadoVersion = findAll();
		for (CertificatesVersion CertiVersion : listCertificadoVersion) {
			String year = String.valueOf(CertiVersion.getFechaImportacion().getYear());
			cvMap.putIfAbsent(year, "");
		}

		for (Entry<String, String> entry : cvMap.entrySet()) {
			cvList.add(entry.getKey());
		}

		return cvList;
	}

	@Override
	public void save(Long id, CertificatesVersionDto dto) {
		CertificatesVersion certiVersion;
		certiVersion = this.findById(id);
		if (certiVersion == null){
			String respuestaEr = "Error al guardar la versión del certificado. El id no existe";
			CapabilityLogger.logError(respuestaEr);
			throw new MyBadAdviceException(respuestaEr);
		}

		BeanUtils.copyProperties(dto, certiVersion, "id");
		this.certificatesVersionRepository.save(certiVersion);

	}

}
