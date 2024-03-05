package com.ccsw.dashboard.versioncapacidades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.versioncapacidades.model.VersionCapacidades;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VersionCapatidadesRepository extends JpaRepository<VersionCapacidades, Long> {
}