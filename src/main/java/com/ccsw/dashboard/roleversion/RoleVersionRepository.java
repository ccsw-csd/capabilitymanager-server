package com.ccsw.dashboard.roleversion;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.roleversion.model.RoleVersion;

@Repository
public interface RoleVersionRepository extends JpaRepository<RoleVersion, Long> {

	Optional<RoleVersion> findById(Long id);
	

}
