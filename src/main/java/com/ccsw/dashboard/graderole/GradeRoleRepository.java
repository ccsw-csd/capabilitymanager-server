package com.ccsw.dashboard.graderole;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.graderole.model.GradeRole;




@Repository
public interface GradeRoleRepository extends CrudRepository<GradeRole, Long>, JpaRepository<GradeRole, Long> {

	List <GradeRole> findAllByRole(String role);
	List <GradeRole> findAllByGrade(String grade);
	List <GradeRole> findAllByRoleAndGrade(String role, String grade);

}
