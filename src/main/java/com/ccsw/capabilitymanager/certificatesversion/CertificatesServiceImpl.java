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
	private static final String ERROR_INIT = ">>> [ERROR][CertificatesServiceImpl] (";
	@Autowired
	private CertificatesVersionRepository certificatesVersionRepository;

	/**
	 * Retrieves a list of all certificate versions, sorted in natural order.
	 *
	 * @return A list of {@link CertificatesVersion} objects representing all certificate versions, sorted by their natural ordering.
	 */
	@Override
	public List<CertificatesVersion> findAll() {
		return (List<CertificatesVersion>) this.certificatesVersionRepository.findAll().stream().sorted().toList();
	}
	
	/**
	 * Retrieves a certificate version by its ID.
	 *
	 * @param id The ID of the certificate version to retrieve.
	 * @return The {@link CertificatesVersion} object with the specified ID, or {@code null} if no such certificate version exists.
	 */
	@Override
	public CertificatesVersion findById(Long id) {
		return this.certificatesVersionRepository.findById(id).orElse(null);
	}

	/**
	 * Retrieves a list of unique years from the certificate versions based on their import dates.
	 *
	 * @return A list of {@link String} representing the unique years in which certificate versions were imported.
	 */
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

	/**
	 * Updates an existing certificate version with the details from the provided {@link CertificatesVersionDto}.
	 * If the certificate version with the specified ID does not exist, an exception is thrown.
	 *
	 * @param id  The ID of the certificate version to update.
	 * @param dto The {@link CertificatesVersionDto} object containing the details to update the certificate version with.
	 * @throws MyBadAdviceException If the certificate version with the specified ID does not exist.
	 */
	@Override
	public void save(Long id, CertificatesVersionDto dto) {
		CertificatesVersion certiVersion;
		certiVersion = this.findById(id);
		if (certiVersion == null){
			String respuestaEr = ERROR_INIT + "save): Error al guardar la versión del certificado. El id no existe";
			CapabilityLogger.logError(respuestaEr);
			throw new MyBadAdviceException(respuestaEr);
		}

		BeanUtils.copyProperties(dto, certiVersion, "id");
		this.certificatesVersionRepository.save(certiVersion);

	}

}
