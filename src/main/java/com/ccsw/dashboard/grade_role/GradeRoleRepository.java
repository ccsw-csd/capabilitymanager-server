package com.ccsw.dashboard.grade_role;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.grade_role.model.GradeRole;




@Repository
public interface GradeRoleRepository extends CrudRepository<GradeRole, Long>, JpaRepository<GradeRole, Long> {

	List <GradeRole> findAllByRole(String role);
	List <GradeRole> findAllByGrade(String grade);
	List <GradeRole> findAllByRoleAndGrade(String role, String grade);

}
