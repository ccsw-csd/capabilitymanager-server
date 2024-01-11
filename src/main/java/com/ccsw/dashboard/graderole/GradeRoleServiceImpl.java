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
import com.ccsw.dashboard.roleversion.RoleVersionService;
import com.ccsw.dashboard.roleversion.model.RoleVersion;

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
    
    @Autowired
    private RoleVersionService roleVersionService;
    
    @Override
    public List<GradeRole> findAll(int idImport) {
    	RoleVersion rv = roleVersionService.findById(Long.valueOf(idImport));
        return (List<GradeRole>) this.gradeRoleRepository.findAll().stream().filter(gr->gr.getIdImport()==idImport).filter(gr->gr.getIdImportStaffing()==rv.getIdVersionStaffing()).toList();
    }

	@Override
	public List<GradeRoleTotal> findAlll(int idImport) {
		
		Map<String, Map<String, Long>> gradeRoleMap = this.findAll(idImport).stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZerosAndOrdGradeRole(gradeRoleMap, gradeService.findAll(), roleService.findAll());
		return LinkedtoList(sortedGradeRolMap);
	}
	
	@Override
	public List<GradeTotal> findAllGradeTotals(int idImport) {
		Map<String, Map<String, Long>> gradeRoleMap = this.findAll(idImport).stream().collect(Collectors.groupingBy(GradeRole::getGrade, Collectors.groupingBy(GradeRole::getRole, Collectors.counting())));
		LinkedHashMap<String, LinkedHashMap<String, Long>> sortedGradeRolMap = addZerosAndOrd(gradeRoleMap, getLiteralGrades(), getLiteralRoles());
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
	
	private LinkedHashMap<String, LinkedHashMap<String, Long>> addZerosAndOrd(Map<String, Map<String, Long>> gradeRoleMap, List<Literal> grades, List<Literal> roles) {
		
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
					if (entry.getKey().equals(role.getDesc())) { // if (((String)entry.getKey()).contains(role.getDesc())) {
	                    sortedRoleMap.put(key, val);
	                }				
				}
			}			
			
			gradeRoleMap.put(grade.getDesc(), sortedRoleMap);
			sortedGradeRoleMap.put(grade.getDesc(), sortedRoleMap);
		}
		
		return sortedGradeRoleMap;
	}
	
private LinkedHashMap<String, LinkedHashMap<String, Long>> addZerosAndOrdGradeRole(Map<String, Map<String, Long>> gradeRoleMap, List<Grade> grades, List<Role> roles) {
		
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
	public List<Literal> getLiteralGrades() {
		return literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "r");
	}
	
	@Override
	public List<Literal> getLiteralRoles() {
		return literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "c");
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
