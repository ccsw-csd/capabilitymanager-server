package com.ccsw.dashboard.grade_role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.RoleDto;
import com.ccsw.dashboard.grade_role.model.GradeRole;
import com.ccsw.dashboard.grade_role.model.GradeRoleAll;
import com.ccsw.dashboard.grade_role.model.GradeRoleAllDto;
import com.ccsw.dashboard.grade_role.model.GradeRoleTotal;
import com.ccsw.dashboard.grade_role.model.GradeRoleTotalDto;


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
		List<GradeRoleTotalDto> gradeRolListDto = new ArrayList<GradeRoleTotalDto>();
		List<GradeRoleTotal> gradeRole = findAlll.getGradeRoleTotal();
		for (GradeRoleTotal gradeRoleTotal : gradeRole) {
			GradeDto gradeDto = grades.stream().filter(g -> g.getGrade().equals(gradeRoleTotal.getGrade())).limit(1).toList().get(0);
			RoleDto roleDto = roles.stream().filter(g -> g.getRole().equals(gradeRoleTotal.getRole())).limit(1).toList().get(0);
			GradeRoleTotalDto gradeRoleTotalDto = new GradeRoleTotalDto(gradeDto, roleDto, gradeRoleTotal.getTotal());
			gradeRolListDto.add(gradeRoleTotalDto);
		}		
		gradeRoleAllDto.setGradeRole(gradeRolListDto);
		return gradeRoleAllDto;
    }    
}
