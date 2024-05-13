package com.ccsw.capabilitymanager.versioncertificados;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VersionCertificacionesRepository extends JpaRepository<VersionCertificaciones, Long> {
}