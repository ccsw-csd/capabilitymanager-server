package com.ccsw.capabilitymanager.versioncapacidades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;

@Repository
@Transactional
public interface VersionCapacidadesRepository extends JpaRepository<VersionCapacidades, Long> {
}