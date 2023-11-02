package com.ccsw.dashboard.graderole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.grade.GradeService;
import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.literal.LiteralService;
import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.config.role.RoleService;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.graderole.model.GradeRole;
import com.ccsw.dashboard.graderole.model.GradeRoleTotal;
import com.ccsw.dashboard.graderole.model.GradeTotal;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class GradeRoleServiceImpl implements GradeRoleService{

    @Autowired
    private GradeRoleRepository gradeRoleRepository;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private LiteralService literalService;
    
    @Override
    public List<GradeRole> findAll() {
        return (List<GradeRole>) this.gradeRoleRepository.findAll();
    }

	@Override
	public List<GradeRoleTotal> findAlll() {
		
		Map<String, Map<String, Long>> gradeRoleMap = this.gradeRoleRepository.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
		//LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZeros(gradeRoleMap, gradeService.findAll(), roleService.findAll());
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZeros(gradeRoleMap, literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "r"), literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "c"));
		return LinkedtoList(sortedGradeRolMap);
	}
	
	@Override
	public List<GradeTotal> findAllGradeTotals() {
		Map<String, Map<String, Long>> gradeRoleMap = this.gradeRoleRepository.findAll().stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
		//LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZeros(gradeRoleMap, gradeService.findAll(), roleService.findAll());
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZeros(gradeRoleMap, literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "r"), literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "c"));
		ArrayList<GradeTotal> gradeTotalList = new ArrayList<GradeTotal>();		
		for (Map.Entry<String, LinkedHashMap<String, Long>> entry : sortedGradeRolMap.entrySet()) {
			String key = entry.getKey();
			LinkedHashMap<String, Long> val = entry.getValue();
			GradeTotal gradeTotal = new GradeTotal();
			ArrayList<Long> totals = new ArrayList<Long>();
			for (Map.Entry<String, Long> rol : val.entrySet()) {								
				totals.add(rol.getValue());				
			}
			gradeTotal.setGrade(key);
			gradeTotal.setTotals(totals);
			gradeTotalList.add(gradeTotal);
		}		
		return gradeTotalList;
	}
	
	private LinkedHashMap<String, LinkedHashMap<String, Long>> addZeros(Map<String, Map<String, Long>> gradeRoleMap, List<Literal> grades, List<Literal> roles) {
		
		HashMap<String, Long> hashMapZeros = new HashMap<String, Long>();
		for (Literal role : roles) {			
			hashMapZeros.put(role.getDesc(), 0L);
		}		
		
		for (Literal grade : grades) {								
			gradeRoleMap.putIfAbsent(grade.getDesc(), hashMapZeros);
			Map<String, Long> roleMap = gradeRoleMap.get(grade.getDesc());
			for (Literal role : roles) {
				roleMap.putIfAbsent(role.getDesc(), 0L);				
			}
		}		
		
		roles = roles.stream().sorted().toList();
		grades = grades.stream().sorted().collect(Collectors.toList());		
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRoleMap = new LinkedHashMap<>();
		for (Literal grade : grades) {
			LinkedHashMap<String, Long> sortedRoleMap = new LinkedHashMap<>();
			Map<String, Long> roleMap = gradeRoleMap.get(grade.getDesc());
			
			/*
			sortedRoleMap = roleMap.entrySet().stream()
	                .sorted(Map.Entry.comparingByKey())
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			*/			
			for (Literal role : roles) {				
				for (Map.Entry entry : roleMap.entrySet()) {
					String key = (String) entry.getKey();
					Long val = (Long) entry.getValue();
					if (entry.getKey().equals(role.getDesc())) {
	                    sortedRoleMap.put(key, val);
	                }				
				}
			}			
			
			gradeRoleMap.put(grade.getDesc(), sortedRoleMap);
			sortedGradeRoleMap.put(grade.getDesc(), sortedRoleMap);
		}
		
		return sortedGradeRoleMap;
	}
	
	private List<GradeRoleTotal> LinkedtoList(LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole) {
		
		List<GradeRoleTotal> gradeRolList = new ArrayList<GradeRoleTotal>();
		for (Entry<String, LinkedHashMap<String, Long>> entry : gradeRole.entrySet()) {
			String grade = entry.getKey();
			LinkedHashMap<String, Long> val = entry.getValue();			
			for (Map.Entry<String, Long> rol : val.entrySet()) {
				String role = rol.getKey();
				Long count = rol.getValue();
				GradeRoleTotal gradeRoleTotal = new GradeRoleTotal(grade, role, count);
				gradeRolList.add(gradeRoleTotal);
			}
		}
		return gradeRolList;
	}
	
	@Override
	public List<Grade> getGrades() {
		return gradeService.findAll();
	}
	
	@Override
	public List<Role> getRoles() {
		return roleService.findAll();
	}
}
