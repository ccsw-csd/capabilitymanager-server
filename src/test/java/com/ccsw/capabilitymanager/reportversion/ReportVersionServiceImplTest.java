package com.ccsw.capabilitymanager.reportversion;

import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.formdataimport.model.FormDataImport;
import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.ReportVersionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportVersionServiceImplTest {
    @InjectMocks
    private ReportVersionServiceImpl reportVersionService;

    @Mock
    private ReportVersionRepository reportVersionRepository;
    
    @Mock
	private CertificatesDataImportRepository certificatesDataImportRepository;
    
    @Mock
    private FormDataImportRepository formDataImportRepository;

    @Mock
	private CertificatesRolesVersionRepository certificatesRolesVersionRepository;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<ReportVersion> mockReportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        mockReportVersions.add(reportVersion);

        when(reportVersionRepository.findAll()).thenReturn(mockReportVersions);

        List<ReportVersion> result = reportVersionService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testFindByIdVersionCapacidades() {
        Long id = 1L;
        ReportVersion mockReportVersion = new ReportVersion();
        mockReportVersion.setId(id);

        when(reportVersionRepository.findByIdVersionCapacidades(anyLong())).thenReturn(java.util.Optional.of(mockReportVersion));

        ReportVersion result = reportVersionService.findByIdVersionCapacidades(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindByScreenshot_WithYear() {
        String screenshot = "1";
        String year = "2024";

        List<ReportVersion> mockReportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        mockReportVersions.add(reportVersion);

        when(reportVersionRepository.findByScreenshotAndFechaImportacionBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockReportVersions);

        List<ReportVersion> result = reportVersionService.findByScreenshot(screenshot, year);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testFindByScreenshot_WithoutYear() {
        String screenshot = "1";

        List<ReportVersion> mockReportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        mockReportVersions.add(reportVersion);

        when(reportVersionRepository.findByScreenshot(anyString())).thenReturn(mockReportVersions);

        List<ReportVersion> result = reportVersionService.findByScreenshot(screenshot, null);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testFindYears_WithScreenshot() {
        String screenshot = "1";
        List<ReportVersion> mockReportVersions = new ArrayList<>();
        ReportVersion reportVersion1 = new ReportVersion();
        reportVersion1.setId(1L);
        reportVersion1.setFechaImportacion(LocalDateTime.of(2024, 1, 1, 0, 0));
        reportVersion1.setScreenshot(1);
        mockReportVersions.add(reportVersion1);

        when(reportVersionRepository.findByScreenshot(screenshot)).thenReturn(mockReportVersions);

        List<String> result = reportVersionService.findYears(screenshot);

        assertEquals(1, result.size());
        assertEquals("2024", result.get(0));
    }

    @Test
    void testFindYears_WithoutScreenshot() {
        // Given
        List<ReportVersion> mockReportVersions = new ArrayList<>();
        ReportVersion reportVersion1 = new ReportVersion();
        reportVersion1.setId(1L);
        reportVersion1.setFechaImportacion(LocalDateTime.of(2024, 1, 1, 0, 0));
        reportVersion1.setScreenshot(1);
        mockReportVersions.add(reportVersion1);

        ReportVersion reportVersion2 = new ReportVersion();
        reportVersion2.setId(2L);
        reportVersion2.setFechaImportacion(LocalDateTime.of(2023, 1, 1, 0, 0));
        reportVersion2.setScreenshot(2);
        mockReportVersions.add(reportVersion2);

        when(reportVersionRepository.findAll()).thenReturn(mockReportVersions);

        List<String> result = reportVersionService.findYears(null);

        assertEquals(2, result.size());
        assertTrue(result.contains("2024"));
        assertTrue(result.contains("2023"));
    }

    @Test
    void testSave_NewReportVersion() {
        Long id = 1L;
        ReportVersionDto dto = new ReportVersionDto();
        dto.setScreenshot(1);
        dto.setUsuario("user");

        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setScreenshot(1);
        reportVersion.setUsuario("user");

        when(reportVersionRepository.findById(anyLong())).thenReturn(Optional.of(reportVersion));

        reportVersionService.save(id, dto);
    }

    @Test
    void testSave_UpdateReportVersion() {
        Long id = 1L;
        ReportVersionDto dto = new ReportVersionDto();
        dto.setScreenshot(1);
        dto.setUsuario("user");

        ReportVersion existingReportVersion = new ReportVersion();
        existingReportVersion.setId(id);
        existingReportVersion.setScreenshot(1);

        when(reportVersionRepository.findById(anyLong())).thenReturn(java.util.Optional.of(existingReportVersion));
        when(reportVersionRepository.save(any(ReportVersion.class))).thenReturn(existingReportVersion);

        reportVersionService.save(id, dto);
    }

    @Test
    void testGenerateReport() {
        GenerateReportVersionDto dto = new GenerateReportVersionDto();
        dto.setIdRoleVersion(1);
        dto.setIdStaffingVersion(1);
        dto.setFescription("description");
        dto.setComments("comments");
        dto.setUser("user");

        ReportVersion generatedReportVersion = new ReportVersion();
        generatedReportVersion.setId(1L);
        
        List<CertificatesDataImport> lista = new ArrayList<>();
        CertificatesDataImport certificate = new CertificatesDataImport();

        // Setea los valores
        certificate.setId(1);
        certificate.setSAGA("SagaTest");
        certificate.setPartner("PartnerTest");
        certificate.setCertificado("CertificadoTest");
        certificate.setNameGTD("NameGTDTest");
        certificate.setCertificationGTD("CertificationGTDTest");
        certificate.setCode("CodeTest");
        certificate.setSector("SectorTest");
        certificate.setModulo("ModuloTest");
        certificate.setIdCandidato("IdCandidatoTest");
        certificate.setFechaCertificado(new Date());
        certificate.setFechaExpiracion(new Date());
        certificate.setActivo("ActivoTest");
        certificate.setAnexo("AnexoTest");
        certificate.setComentarioAnexo("ComentarioAnexoTest");
        certificate.setNumImportCodeId(1001);
        certificate.setGgid("GgidTest");
    
    
        lista.add(certificate);
        
        FormDataImport imp = new FormDataImport();
        imp.setId(1);
        imp.setNumImportCodeId(1001);
        imp.setSAGA("SagaTest");
        imp.setEmail("test@example.com");
        imp.setName("John Doe");
        imp.setVcProfileRolL1("Role L1");
        imp.setRolL1Extendido("Role L1 Extended");
        imp.setRolL2EM("Role L2 EM");
        imp.setRolL2AR("Role L2 AR");
        imp.setRolL2AN("Role L2 AN");
        imp.setRolL2SE("Role L2 SE");
        imp.setRolL3("Role L3");
        imp.setRolL4("Role L4");
        imp.setRolExperienceEM("Experience EM");
        imp.setRolExperienceAR("Experience AR");
        imp.setSkillCloudNativeExperience("Cloud Native Experience");
        imp.setSkillLowCodeExperience("Low Code Experience");
        imp.setSectorExperience("Sector Experience");
        imp.setSkillCloudExp("Cloud Experience");
        imp.setRolL1("architect");
        
        when(formDataImportRepository.findBySAGAAndNumImportCodeId(anyString(),anyInt())).thenReturn(imp);
        
        when(reportVersionRepository.save(any(ReportVersion.class))).thenReturn(generatedReportVersion);
    
        when(certificatesDataImportRepository.findByNumImportCodeId(anyLong())).thenReturn(lista);
        

        ReportVersion result = reportVersionService.generateReport(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
