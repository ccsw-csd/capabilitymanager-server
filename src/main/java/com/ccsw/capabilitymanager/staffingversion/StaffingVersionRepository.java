package com.ccsw.capabilitymanager.staffingversion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;

@Repository
public interface StaffingVersionRepository extends JpaRepository<StaffingVersion, Long> {

	Optional<StaffingVersion> findById(Long id);
	

}
