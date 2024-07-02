package com.ccsw.capabilitymanager.reportversion;

import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.ReportVersionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ReportVersionServiceImplTest {
    @InjectMocks
    private ReportVersionServiceImpl reportVersionService;

    @Mock
    private ReportVersionRepository reportVersionRepository;

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

        when(reportVersionRepository.save(any(ReportVersion.class))).thenReturn(generatedReportVersion);

        ReportVersion result = reportVersionService.generateReport(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
