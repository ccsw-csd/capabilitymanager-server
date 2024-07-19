package com.ccsw.capabilitymanager.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.config.literal.LiteralService;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.graderole.GradeRoleService;
import com.ccsw.capabilitymanager.graderole.model.GradeRole;
import com.ccsw.capabilitymanager.graderole.model.GradeTotal;
import com.ccsw.capabilitymanager.profile.model.InformeRoles;
import com.ccsw.capabilitymanager.profile.model.Profile;
import com.ccsw.capabilitymanager.profile.model.ProfileGroup;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;
import com.ccsw.capabilitymanager.reportversion.ReportVersionService;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.views.service.CounterSummaryService;
import com.ccsw.capabilitymanager.views.service.ViewGradosRolesService;

public class ProfileServiceImplTest {

	@InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private LiteralService literalService;     

    @Mock
    private GradeRoleService gradeRoleService;

    @Mock
    private ReportVersionService reportVersionService;

    @Mock
    private CounterSummaryService counterSummaryService;

    @Mock
    private ViewGradosRolesService viewGradosRolesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(2);

        List<Profile> profiles = new ArrayList();
        Profile profile = new Profile();
        profiles.add(profile);

        when(reportVersionService.findById((long) idReport)).thenReturn(reportVersion);
        when(counterSummaryService.recoverCounterSummaryAll(1, 2)).thenReturn(profiles);

        List<Profile> result = profileService.findAll(idReport);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindAllActual() {
        String actual = "actual";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(2);

        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profiles.add(profile);

        when(reportVersionService.findById((long) idReport)).thenReturn(reportVersion);
        when(counterSummaryService.recoverCounterSummary(1, 2, actual)).thenReturn(profiles);

        List<Profile> result = profileService.findAllActual(actual, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testObtenerVersionCertificaciones() {
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCertificaciones(1);

        when(reportVersionService.findById((long) idReport)).thenReturn(reportVersion);

        profileService.obtenerVersionCertificaciones(idReport);

        verify(counterSummaryService, times(1)).obtenerVersionCertificaciones(1);
    }

    @Test
    public void testFindAllInformeRoles() {
        int idReport = 1;
        String id = "Architects";
        ReportVersion reportVersion = new ReportVersion();

        List<ProfileTotal> profileTotals = new ArrayList<>();
        ProfileTotal profileTotal = new ProfileTotal();
        profileTotal.setProfile("asd");
        ArrayList<Long> total = new ArrayList<>();
        total.add(1l);
        total.add(2l);
        profileTotal.setTotals(total);
        profileTotals.add(profileTotal);

        List<Literal> literals = new ArrayList<>();
        Literal literal = new Literal();
        literal.setId(1l);
        literal.setType("Type");
        literal.setSubtype("Sub");
        literal.setOrd(1);
        literal.setDesc("Des");
        Literal literal2 = new Literal();
        literal2.setId(2l);
        literal2.setType("Type");
        literal2.setSubtype("Sub");
        literal2.setOrd(1);
        literal2.setDesc("Des");
        literals.add(literal);
        literals.add(literal2);
        List<GradeTotal> gradeTotals = new ArrayList<>();
        GradeTotal gradeTotal = new GradeTotal();
        gradeTotals.add(gradeTotal);
        
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(counterSummaryService.recoverCounterSummary(anyInt(), anyInt(), anyString())).thenReturn(new ArrayList<>());
        when(gradeRoleService.findAllGradeTotals(anyInt())).thenReturn(gradeTotals);

        InformeRoles result = profileService.findAllInformeRoles(idReport);

        assertNotNull(result);
        assertEquals(2, result.getArchitects().size());
        assertEquals(2, result.getSoftwareEngineer().size());
        assertEquals(2, result.getIndustryExperts().size());
        assertEquals(2, result.getEngagementManagers().size());
        assertEquals(2, result.getBusinessAnalyst().size());
        assertEquals(2, result.getArchitectsCustomApps().size());
        assertEquals(2, result.getArchitectsIntegration().size());
        assertEquals(1, result.getGradeTotal().size());
    }
    
    @Test
    public void testFindAllProfileTotals_PyramidGradeRol() {
        int idReport = 1;
        List<GradeTotal> gradeTotals = new ArrayList<>();
        GradeTotal gradeTotal = new GradeTotal();
        gradeTotal.setGrade("Grade");
        ArrayList<Long> total = new ArrayList<>();
        total.add(1l);
        total.add(2l);
        gradeTotal.setTotals(total);
        gradeTotals.add(gradeTotal);

        when(gradeRoleService.findAllGradeTotals(anyInt())).thenReturn(gradeTotals);

        List<ProfileTotal> result = profileService.findAllProfileTotals("Pyramid Grade-Rol", idReport);

        assertNotNull(result);
        assertEquals(1, result.size());
        // Añadir más aserciones si es necesario
    }

    @Test
    public void testFindAllProfileTotals_All() {
        int idReport = 1;
        List<Literal> literals = new ArrayList<>();
        Literal literal = new Literal();
        literals.add(literal);
        ReportVersion reportVersion = new ReportVersion();
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setGgid("GGID1234");
        profile.setSaga("Saga Example");
        profile.setPractica("Practica Example");
        profile.setGrado("Grado Example");
        profile.setCategoria("Categoria Example");
        profile.setCentro("Centro Example");
        profile.setNombre("Nombre Example");
        profile.setEmail("example@example.com");
        profile.setLocalizacion("Localizacion Example");
        profile.setStatus("Status Example");
        profile.setPerfilStaffing("Perfil Staffing Example");
        profile.setActual("Actual Example");
        profile.setPerfil("Perfil Example");
        profile.setExperiencia("Experiencia Example");
        profile.setCertificaciones("Certificaciones Example");
        profile.setTecnicoSolution("Tecnico Solution Example");
        profile.setTecnicoIntegration("Tecnico Integration Example");
        profile.setSkillCloudNative("Skill Cloud Native Example");
        profile.setSkillCloudNativeExperiencia("Skill Cloud Native Experiencia Example");
        profile.setSkillLowCode("Skill Low Code Example");
        profile.setSectorExperiencia("Sector Experiencia Example");
        profile.setIdImportCapacidades(123);
        profile.setIdImportStaffing(456);
        profile.setIdVersionCertificaciones(789);
        profiles.add(profile);

        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileTotal> result = profileService.findAllProfileTotals("All", idReport);

        assertNotNull(result);
        assertEquals(1, result.size());
        // Añadir más aserciones si es necesario
    }

    @Test
    public void testFindAllProfileTotals_InvalidInput() {
        int idReport = 1;

        MyBadAdviceException exception = assertThrows(MyBadAdviceException.class, () -> {
            profileService.findAllProfileTotals("Invalid Input", idReport);
        });

        assertEquals("entrada no válida", exception.getMessage());
    }
    
    @Test
    public void testFindAllProfile_EngagementManagers() {
        String id = "Engagement Managers";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal(), new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setPerfil("Engagement Managers");
        profiles.add(profile);
        
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(2, result.size());

    }
    
    @Test
    public void testFindAllProfile_SoftwareEngineer() {
        String id = "Software Engineer";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal(), new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setPerfil("Software Engineer");
        profiles.add(profile);
        
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(2, result.size());

    }
    
    @Test
    public void testFindAllProfile_IndustryExperts() {
        String id = "Industry Experts";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();
        // Simular perfiles con diferentes sectores de experiencia
        profiles.add(createProfile("John Doe", "Consumer"));
        profiles.add(createProfile("Jane Smith", "Energy & Utilities"));
        profiles.add(createProfile("Michael Johnson", "Manufacturing"));
        profiles.add(createProfile("Emily Brown", "Life Science"));
        profiles.add(createProfile("David Lee", "Public Sector"));
        profiles.add(createProfile("Sarah Clark", "Telco"));
        profiles.add(createProfile("Robert Wilson", "Financial Ser"));

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testFindAllProfile_ArchitectsAndSECustomAppsDevelopment() {
        String id = "Architects & SE Custom Apps Development";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setGgid("GGID1234");
        profile.setSaga("Saga Example");
        profile.setPractica("Practica Example");
        profile.setGrado("Grado Example");
        profile.setCategoria("Categoria Example");
        profile.setCentro("Centro Example");
        profile.setNombre("Nombre Example");
        profile.setEmail("example@example.com");
        profile.setLocalizacion("Localizacion Example");
        profile.setStatus("Status Example");
        profile.setPerfilStaffing("Perfil Staffing Example");
        profile.setActual("Actual Example");
        profile.setPerfil("Perfil Example");
        profile.setExperiencia("Experiencia Example");
        profile.setCertificaciones("Certificaciones Example");
        profile.setTecnicoSolution("Tecnico Solution Example");
        profile.setTecnicoIntegration("Tecnico Integration Example");
        profile.setSkillCloudNative("Skill Cloud Native Example");
        profile.setSkillCloudNativeExperiencia("Skill Cloud Native Experiencia Example");
        profile.setSkillLowCode("Skill Low Code Example");
        profile.setSectorExperiencia("Sector Experiencia Example");
        profile.setIdImportCapacidades(123);
        profile.setIdImportStaffing(456);
        profile.setIdVersionCertificaciones(789);
        profiles.add(profile);

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());

    }
    
    @Test
    public void testFindAllProfile_ArchitectsAndSEIntegrationAndApis() {
        String id = "Architects & SE Integration & APIs";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setGgid("GGID1234");
        profile.setSaga("Saga Example");
        profile.setPractica("Practica Example");
        profile.setGrado("Grado Example");
        profile.setCategoria("Categoria Example");
        profile.setCentro("Centro Example");
        profile.setNombre("Nombre Example");
        profile.setEmail("example@example.com");
        profile.setLocalizacion("Localizacion Example");
        profile.setStatus("Status Example");
        profile.setPerfilStaffing("Perfil Staffing Example");
        profile.setActual("Actual Example");
        profile.setPerfil("Perfil Example");
        profile.setExperiencia("Experiencia Example");
        profile.setCertificaciones("Certificaciones Example");
        profile.setTecnicoSolution("Tecnico Solution Example");
        profile.setTecnicoIntegration("Tecnico Integration Example");
        profile.setSkillCloudNative("Skill Cloud Native Example");
        profile.setSkillCloudNativeExperiencia("Skill Cloud Native Experiencia Example");
        profile.setSkillLowCode("Skill Low Code Example");
        profile.setSectorExperiencia("Sector Experiencia Example");
        profile.setIdImportCapacidades(123);
        profile.setIdImportStaffing(456);
        profile.setIdVersionCertificaciones(789);
        profiles.add(profile);

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    // Método utilitario para crear un perfil con sector de experiencia
    private Profile createProfile(String nombre, String sectorExperiencia) {
        Profile profile = new Profile();
        profile.setNombre(nombre);
        profile.setSectorExperiencia(sectorExperiencia);
        return profile;
    }

    @Test
    public void testFindAllProfile_Architects() {
        String id = "Architects";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setPerfil("Architect Group");
        profiles.add(profile);

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);
        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    public void testFindAllProfile_BusinessAnalyst() {
        String id = "Business Analyst";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    public void testFindAllProfile_PyramidGradeRol() {
        String id = "Pyramid Grade-Rol";
        int idReport = 1;

        List<Literal> literals = Arrays.asList(new Literal(), new Literal());
        List<Profile> profiles = new ArrayList<>();
        Profile profileA = new Profile();
        profileA.setGrado("Grade A");
        Profile profileB = new Profile();
        profileB.setGrado("Grade B");
        profiles.add(profileA);
        profiles.add(profileB);

        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);

        List<GradeRole> gradeRoles = Arrays.asList(new GradeRole(), new GradeRole());

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(viewGradosRolesService.getAll(anyInt(), anyInt())).thenReturn(gradeRoles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(2, result.size());


    }

    @Test
    public void testFindAllProfile_AllProfiles() {
        String id = "All Profiles";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();

        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        List<ProfileGroup> result = profileService.findAllProfile(id, idReport);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    public void testFindAllProfile_InvalidEntry() {
        String id = "Invalid";
        int idReport = 1;
        ReportVersion reportVersion = new ReportVersion();
        List<Literal> literals = Arrays.asList(new Literal());
        List<Profile> profiles = new ArrayList<>();
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(reportVersionService.findById(anyLong())).thenReturn(reportVersion);
        when(literalService.findByTypeAndSubtype(anyString(), anyString())).thenReturn(literals);
        when(counterSummaryService.recoverCounterSummaryAll(anyInt(), anyInt())).thenReturn(profiles);

        
        Exception exception = assertThrows(MyBadAdviceException.class, () -> {
            profileService.findAllProfile(id, idReport);
        });

        String expectedMessage = "entrada no válida";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

   
}