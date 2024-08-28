package com.ccsw.capabilitymanager.graderole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ccsw.capabilitymanager.config.grade.model.Grade;
import com.ccsw.capabilitymanager.config.grade.model.GradeDto;
import com.ccsw.capabilitymanager.config.role.model.Role;
import com.ccsw.capabilitymanager.config.role.model.RoleDto;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotal;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotalDto;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GradeRoleControllerTest {
    @InjectMocks
    private GradeRoleController gradeRoleController;

    @Mock
    private GradeRoleService gradeRoleService;

    @Mock
    private DozerBeanMapper mapper;

    private GradeRole gradeRole1;
    private GradeRole gradeRole2;
    private GradeRoleTotal gradeRoleTotal1;
    private GradeRoleTotal gradeRoleTotal2;
    private GradeDto gradeDto1;
    private GradeDto gradeDto2;
    private RoleDto roleDto1;
    private RoleDto roleDto2;
    private Grade grade1;
    private Grade grade2;
    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        gradeRole1 = new GradeRole();
        gradeRole1.setGrade("Grade 1");
        gradeRole1.setRole("Role 1");

        gradeRole2 = new GradeRole();
        gradeRole2.setGrade("Grade 2");
        gradeRole2.setRole("Role 2");

        grade1 = new Grade();
        grade1.setGrade("Grade 1");

        grade2 = new Grade();
        grade2.setGrade("Grade 2");

        role1 = new Role();
        role1.setRole("Role 1");

        role2 = new Role();
        role2.setRole("Role 2");

        gradeRoleTotal1 = new GradeRoleTotal("Grade 1", "Role 1", 10L);
        gradeRoleTotal2 = new GradeRoleTotal("Grade 2", "Role 2", 20L);

        gradeDto1 = new GradeDto();
        gradeDto1.setGrade("Grade 1");

        gradeDto2 = new GradeDto();
        gradeDto2.setGrade("Grade 2");

        roleDto1 = new RoleDto();
        roleDto1.setRole("Role 1");

        roleDto2 = new RoleDto();
        roleDto2.setRole("Role 2");
    }

    @Test
    public void testFindAllDb() {
        int idReport = 1;
        List<GradeRole> gradeRoles = List.of(gradeRole1, gradeRole2);

        when(gradeRoleService.findAll(idReport)).thenReturn(gradeRoles);

        Map<String, Map<String, Long>> result = gradeRoleController.findAllDb(idReport);

        assertEquals(1L, result.get("Grade 1").get("Role 1"));
        assertEquals(1L, result.get("Grade 2").get("Role 2"));
    }

    @Test
    public void testFindAll() {
        int idReport = 1;
        List<GradeRoleTotal> gradeRoleTotals = List.of(gradeRoleTotal1, gradeRoleTotal2);

        when(gradeRoleService.findAlll(idReport)).thenReturn(gradeRoleTotals);

        List<GradeRoleTotal> result = gradeRoleController.findAll(idReport);

        assertEquals(2, result.size());
        assertEquals(gradeRoleTotal1, result.get(0));
        assertEquals(gradeRoleTotal2, result.get(1));
    }

    @Test
    public void testFindAlll() {
        int idReport = 1;
        List<GradeRoleTotal> gradeRoleTotals = List.of(gradeRoleTotal1, gradeRoleTotal2);
        List<Grade> grades = List.of(grade1, grade2);
        List<Role> roles = List.of(role1, role2);

        when(gradeRoleService.findAlll(idReport)).thenReturn(gradeRoleTotals);
        when(gradeRoleService.getGrades()).thenReturn(grades);
        when(gradeRoleService.getRoles()).thenReturn(roles);
        when(mapper.map(grade1, GradeDto.class)).thenReturn(gradeDto1);
        when(mapper.map(grade2, GradeDto.class)).thenReturn(gradeDto2);
        when(mapper.map(role1, RoleDto.class)).thenReturn(roleDto1);
        when(mapper.map(role2, RoleDto.class)).thenReturn(roleDto2);

        List<GradeRoleTotalDto> result = gradeRoleController.findAlll(idReport);

        assertEquals(2, result.size());
        assertEquals("Grade 1", result.get(0).getGrade().getGrade());
        assertEquals("Role 1", result.get(0).getRole().getRole());
        assertEquals(10L, result.get(0).getTotal());
        assertEquals("Grade 2", result.get(1).getGrade().getGrade());
        assertEquals("Role 2", result.get(1).getRole().getRole());
        assertEquals(20L, result.get(1).getTotal());
    }

    @Test
    public void testFindAllGradeTotals() {
        int idReport = 1;
        List<GradeTotal> gradeTotals = new ArrayList<>();

        when(gradeRoleService.findAllGradeTotals(idReport)).thenReturn(gradeTotals);

        List<GradeTotal> result = gradeRoleController.findAllGradeTotals(idReport);

        assertEquals(gradeTotals, result);
    }
}
