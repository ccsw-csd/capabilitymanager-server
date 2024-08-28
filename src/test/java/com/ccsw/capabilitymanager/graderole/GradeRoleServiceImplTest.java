package com.ccsw.capabilitymanager.graderole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.config.grade.GradeService;
import com.ccsw.capabilitymanager.config.grade.model.Grade;
import com.ccsw.capabilitymanager.config.literal.LiteralService;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.config.role.RoleService;
import com.ccsw.capabilitymanager.config.role.model.Role;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeRoleTotal;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;
import com.ccsw.capabilitymanager.reportversion.ReportVersionService;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.views.service.ViewGradosRolesService;

public class GradeRoleServiceImplTest {
    @Mock
    private GradeService gradeService;

    @Mock
    private RoleService roleService;

    @Mock
    private LiteralService literalService;

    @Mock
    private ReportVersionService reportVersionService;

    @Mock
    private ViewGradosRolesService viewGradosRoleService;

    @InjectMocks
    private GradeRoleServiceImpl gradeRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnCollectionOfGradeRoles() {
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);

        when(reportVersionService.findById((long) idReport)).thenReturn(reportVersion);

        List<GradeRole> gradeRoles = new ArrayList<>();
        GradeRole gradeRole = new GradeRole();
        gradeRole.setGrade("A");
        gradeRole.setRole("Developer");

        // Añadirlo a la lista
        gradeRoles.add(gradeRole);

        when(viewGradosRoleService.getAll(1, 1)).thenReturn(gradeRoles);

        Collection<GradeRole> result = gradeRoleService.findAll(idReport);

        assertEquals(gradeRoles.size(), result.size());
        verify(reportVersionService).findById((long) idReport);
        verify(viewGradosRoleService).getAll(1, 1);
    }

    @Test
    void findAlll_shouldReturnListOfGradeRoleTotals() {
        int idReport = 1;

        // Mock para ReportVersion
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);

        // Simulaciones para los métodos de servicio
        when(reportVersionService.findById(Long.valueOf(idReport))).thenReturn(reportVersion);

        // Configuración de datos para grades
        Grade grade = new Grade();
        grade.setGrade("A");
        List<Grade> grades = Collections.singletonList(grade);

        // Configuración de datos para roles
        Role role = new Role();
        role.setRole("Developer");
        List<Role> roles = Collections.singletonList(role);

        // Configuración de datos para gradeRoles
        GradeRole gradeRole = new GradeRole();
        gradeRole.setGrade("A");
        gradeRole.setRole("Developer");
        List<GradeRole> gradeRoles = Collections.singletonList(gradeRole);

        // Mock de servicios adicionales
        when(gradeService.findAll()).thenReturn(grades);
        when(roleService.findAll()).thenReturn(roles);
        when(viewGradosRoleService.getAll(reportVersion.getIdVersionCapacidades(), reportVersion.getIdVersionStaffing())).thenReturn(gradeRoles);

        // Ejecutar el método público que invoca el método privado
        List<GradeRoleTotal> result = gradeRoleService.findAlll(idReport);

        // Verificar resultados
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getGrade());
        assertEquals("Developer", result.get(0).getRole());
        assertEquals(1L, result.get(0).getTotal());
    }

    @Test
    void findAllGradeTotals_shouldReturnListOfGradeTotals() {
        int idReport = 1;

        // Mock para ReportVersion
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);

        // Simulaciones para los métodos de servicio
        when(reportVersionService.findById(Long.valueOf(idReport))).thenReturn(reportVersion);

        // Configuración de datos para los literales de grade
        Literal gradeLiteral = new Literal();
        gradeLiteral.setDesc("A");
        List<Literal> gradeLiterals = Collections.singletonList(gradeLiteral);

        // Configuración de datos para los literales de role
        Literal roleLiteral = new Literal();
        roleLiteral.setDesc("Developer");
        List<Literal> roleLiterals = Collections.singletonList(roleLiteral);

        // Configuración de datos para gradeRoles
        GradeRole gradeRole = new GradeRole();
        gradeRole.setGrade("A");
        gradeRole.setRole("Developer");
        List<GradeRole> gradeRoles = Collections.singletonList(gradeRole);

        // Mock de servicios adicionales
        when(literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "r")).thenReturn(gradeLiterals);
        when(literalService.findByTypeAndSubtype("Pyramid Grade-Rol", "c")).thenReturn(roleLiterals);
        when(viewGradosRoleService.getAll(reportVersion.getIdVersionCapacidades(), reportVersion.getIdVersionStaffing())).thenReturn(gradeRoles);

        // Ejecutar el método público que invoca el método privado
        List<GradeTotal> result = gradeRoleService.findAllGradeTotals(idReport);

        // Verificar resultados
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getGrade());
        assertEquals(1, result.get(0).getTotals().size());
        assertEquals(1L, result.get(0).getTotals().get(0));
    }

    @Test
    void getGrades_shouldReturnListOfGrades() {
        Grade grade = new Grade();
        grade.setGrade("A");
        List<Grade> grades = Collections.singletonList(grade);

        when(gradeService.findAll()).thenReturn(grades);

        List<Grade> result = gradeRoleService.getGrades();

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getGrade());
    }

    @Test
    void getRoles_shouldReturnListOfRoles() {
        Role role = new Role();
        role.setRole("Developer");
        List<Role> roles = Collections.singletonList(role);

        when(roleService.findAll()).thenReturn(roles);

        List<Role> result = gradeRoleService.getRoles();

        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).getRole());
    }

}
