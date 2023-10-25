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

    @RequestMapping(path = "/db", method = RequestMethod.GET)
    public Map<String, Map<String, Long>> findAllDb(){
        return this.gradeRoleService.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
    }
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GradeRoleTotal> findAll(){
        return this.gradeRoleService.findAlll();
    }
    
    @RequestMapping(path = "/objects", method = RequestMethod.GET)
    public List<GradeRoleTotalDto> findAlll(){    	 	
    	List<GradeRoleTotal> GradeRoleTotalList = this.gradeRoleService.findAlll();
		List<GradeDto> grades = this.gradeRoleService.getGrades().stream().map(g -> mapper.map(g, GradeDto.class)).toList();
		List<RoleDto> roles = gradeRoleService.getRoles().stream().map(g -> mapper.map(g, RoleDto.class)).toList();
		List<GradeRoleTotalDto> gradeRolListDto = new ArrayList<GradeRoleTotalDto>();		
		for (GradeRoleTotal gradeRoleTotal : GradeRoleTotalList) {
			GradeDto gradeDto = grades.stream().filter(g -> g.getGrade().equals(gradeRoleTotal.getGrade())).limit(1).toList().get(0);
			RoleDto roleDto = roles.stream().filter(g -> g.getRole().equals(gradeRoleTotal.getRole())).limit(1).toList().get(0);
			GradeRoleTotalDto gradeRoleTotalDto = new GradeRoleTotalDto(gradeDto, roleDto, gradeRoleTotal.getTotal());
			gradeRolListDto.add(gradeRoleTotalDto);
		}
		return gradeRolListDto;
    }    
}
