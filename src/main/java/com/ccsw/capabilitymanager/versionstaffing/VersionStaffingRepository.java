package com.ccsw.capabilitymanager.versionstaffing;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VersionStaffingRepository extends JpaRepository<VersionStaffing, Long> {
}