package com.ccsw.dashboard.grade_role;

import jakarta.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.grade.GradeService;
import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.RoleService;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.config.role.model.RoleDto;
import com.ccsw.dashboard.grade_role.model.GradeRole;
import com.ccsw.dashboard.grade_role.model.GradeRoleAll;
import com.ccsw.dashboard.grade_role.model.GradeRoleDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class GradeRoleServiceImpl implements GradeRoleService{

    @Autowired
    private GradeRoleRepository gradeRoleRepository;
    
    @Autowired
    DozerBeanMapper mapper;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private RoleService roleService;
    
    @Override
    public List<GradeRole> findAll() {
        return (List<GradeRole>) this.gradeRoleRepository.findAll();
    }

	@Override
	public GradeRoleAll findAlll() {
		GradeRoleAll gradeRoleAll = new GradeRoleAll();
		//gradeRoleAll.setGradeRole(this.gradeRoleRepository.findAll().stream().map(gr->mapper.map(gr,GradeRoleDto.class)).toList());
		gradeRoleAll.setGradeRole(this.gradeRoleRepository.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting()))));
		gradeRoleAll.setGrades(gradeService.findAll().stream().map(gr->mapper.map(gr,GradeDto.class)).toList());
		gradeRoleAll.setRoles(roleService.findAll().stream().map(gr->mapper.map(gr,RoleDto.class)).toList());
		return gradeRoleAll;	
	}
    
	  
    
}
