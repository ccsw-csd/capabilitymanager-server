package com.ccsw.capabilitymanager.graderole;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.capabilitymanager.graderole.model.GradeRole;




@Repository
public interface GradeRoleRepository extends JpaRepository<GradeRole, Long> {

	List <GradeRole> findAllByRole(String role);
	List <GradeRole> findAllByGrade(String grade);
	List <GradeRole> findAllByRoleAndGrade(String role, String grade);

}
