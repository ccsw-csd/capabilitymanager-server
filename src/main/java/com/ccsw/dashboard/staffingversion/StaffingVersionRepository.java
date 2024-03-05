package com.ccsw.dashboard.staffingversion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.staffingversion.model.StaffingVersion;

@Repository
public interface StaffingVersionRepository extends CrudRepository<StaffingVersion, Long>, JpaRepository<StaffingVersion, Long> {

	Optional<StaffingVersion> findById(Long id);
	

}
