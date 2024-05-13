package com.ccsw.capabilitymanager.graderole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ccsw.capabilitymanager.config.grade.model.GradeDto;
import com.ccsw.capabilitymanager.config.role.model.RoleDto;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotal;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotalDto;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;

@RequestMapping(value = "/grade-role")
@RestController
public class GradeRoleController {

    @Autowired
    private GradeRoleService gradeRoleService;
    
    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/db")
    public Map<String, Map<String, Long>> findAllDb(@RequestParam(value = "idReport", required = true) int idReport) {
        return this.gradeRoleService.findAll(idReport).stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
    }
    
    @GetMapping("")
    public List<GradeRoleTotal> findAll(@RequestParam(value = "idReport", required = true) int idReport) {
        return this.gradeRoleService.findAlll(idReport);
    }
    
   
    @GetMapping("/objects")
    public List<GradeRoleTotalDto> findAlll(@RequestParam(value = "idReport", required = true) int idReport) {
    	List<GradeRoleTotal> GradeRoleTotalList = this.gradeRoleService.findAlll(idReport);
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
    
    @GetMapping("/gradetotals")
    public List<GradeTotal> findAllGradeTotals(@RequestParam(value = "idReport", required = true) int idReport) {
    	return this.gradeRoleService.findAllGradeTotals(idReport);
    }
    
}
