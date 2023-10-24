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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
		gradeRoleAll.setGrades(gradeService.findAll().stream().map(gr->mapper.map(gr,GradeDto.class)).toList());
		gradeRoleAll.setRoles(roleService.findAll().stream().map(gr->mapper.map(gr,RoleDto.class)).toList());
		Map<String, Map<String, Long>> gradeRoleMap = this.gradeRoleRepository.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZeros(gradeRoleMap, gradeService.findAll(), roleService.findAll());
		gradeRoleAll.setGradeRole(sortedGradeRolMap);
		return gradeRoleAll;
	}
	
	private LinkedHashMap<String, LinkedHashMap<String, Long>> addZeros(Map<String, Map<String, Long>> gradeRoleMap, List<Grade> grades, List<Role> roles) {
		
		HashMap<String, Long> hashMapZeros = new HashMap<String, Long>();
		for (Role role : roles) {			
			hashMapZeros.put(role.getRole(), 0L);
		}		
		
		for (Grade grade : grades) {								
			gradeRoleMap.putIfAbsent(grade.getGrade(), hashMapZeros);
			Map<String, Long> roleMap = gradeRoleMap.get(grade.getGrade());
			for (Role role : roles) {
				roleMap.putIfAbsent(role.getRole(), 0L);				
			}
		}		
		
		roles = roles.stream().sorted().toList();
		grades = grades.stream().sorted().collect(Collectors.toList());		
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRoleMap = new LinkedHashMap<>();
		for (Grade grade : grades) {
			LinkedHashMap<String, Long> sortedRoleMap = new LinkedHashMap<>();
			Map<String, Long> roleMap = gradeRoleMap.get(grade.getGrade());
			
			/*
			sortedRoleMap = roleMap.entrySet().stream()
	                .sorted(Map.Entry.comparingByKey())
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			*/			
			for (Role role : roles) {				
				for (Map.Entry entry : roleMap.entrySet()) {
					String key = (String) entry.getKey();
					Long val = (Long) entry.getValue();
					if (entry.getKey().equals(role.getRole())) {
	                    sortedRoleMap.put(key, val);
	                }				
				}
			}			
			
			gradeRoleMap.put(grade.getGrade(), sortedRoleMap);
			sortedGradeRoleMap.put(grade.getGrade(), sortedRoleMap);
		}
		
		return sortedGradeRoleMap;
	}
}
