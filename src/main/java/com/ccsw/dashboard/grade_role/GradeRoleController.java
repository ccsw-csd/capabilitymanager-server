package com.ccsw.dashboard.grade_role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.config.role.model.RoleDto;
import com.ccsw.dashboard.grade_role.model.GradeRole;
import com.ccsw.dashboard.grade_role.model.GradeRoleAll;
import com.ccsw.dashboard.grade_role.model.GradeRoleAllDto;
import com.ccsw.dashboard.grade_role.model.GradeRoleDto;


@RequestMapping(value = "/grade-role")
@RestController
public class GradeRoleController {

    @Autowired
    private GradeRoleService gradeRoleService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Map<String, Map<String, Long>> findAllBasic(){
        return this.gradeRoleService.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
    }
    
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public GradeRoleAll findAll(){
        return this.gradeRoleService.findAlll();
    }
    
    @RequestMapping(path = "/alll", method = RequestMethod.GET)
    public GradeRoleAllDto findAllll(){
    	GradeRoleAllDto gradeRoleAllDto = new GradeRoleAllDto();    	
    	GradeRoleAll findAlll = this.gradeRoleService.findAlll();
		List<GradeDto> grades = findAlll.getGrades().stream().map(g -> mapper.map(g, GradeDto.class)).toList();
		gradeRoleAllDto.setGrades(grades);
		List<RoleDto> roles = findAlll.getRoles().stream().map(g -> mapper.map(g, RoleDto.class)).toList();
		gradeRoleAllDto.setRoles(roles);
		List<GradeRoleDto> gradeRolListDto = new ArrayList<GradeRoleDto>();
		LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole = findAlll.getGradeRole();
		for (Entry<String, LinkedHashMap<String, Long>> entry : gradeRole.entrySet()) {
			String grade = entry.getKey();
			LinkedHashMap<String, Long> val = entry.getValue();
			LinkedHashMap<RoleDto, Long> linkedHashMap = new LinkedHashMap<RoleDto, Long>();
			for (Map.Entry<String, Long> rol : val.entrySet()) {
				String role = rol.getKey();
				Long count = rol.getValue();
				linkedHashMap.put(roles.stream().filter(r -> r.getRole().equals(role)).limit(1).toList().get(0), count);
				GradeDto gradeDto = grades.stream().filter(g -> g.getGrade().equals(grade)).limit(1).toList().get(0);
				RoleDto roleDto = roles.stream().filter(g -> g.getRole().equals(role)).limit(1).toList().get(0);
				GradeRoleDto gradeRoleDto = new GradeRoleDto(gradeDto, roleDto, count);
				gradeRolListDto.add(gradeRoleDto);
			}
		}
		gradeRoleAllDto.setGradeRole(gradeRolListDto);
		return gradeRoleAllDto;
    }    
}
