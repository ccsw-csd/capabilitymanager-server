package com.ccsw.capabilitymanager.versionitinerarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VersionItinerariosRepository extends JpaRepository<VersionItinerarios, Long> {
}