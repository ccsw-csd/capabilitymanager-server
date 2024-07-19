package com.ccsw.capabilitymanager.reportversion;

import com.ccsw.capabilitymanager.certificatesversion.CertificatesService;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.reportversion.model.GenerateReportVersionDto;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;
import com.ccsw.capabilitymanager.roleversion.RoleVersionService;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;
import com.ccsw.capabilitymanager.staffingversion.StaffingVersionService;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReportVersionControllerTest {
    @InjectMocks
    private ReportVersionController reportVersionController;

    @Mock
    private ReportVersionService reportVersionService;

    @Mock
    private StaffingVersionService staffingVersionService;

    @Mock
    private RoleVersionService roleVersionService;
    
    @Mock
	private CertificatesService certificatesService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<ReportVersion> reportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);
        reportVersion.setIdVersionCertificaciones(1);
        reportVersion.setUsuario("user");
        reportVersion.setDescripcion("description");
        reportVersion.setScreenshot(1);
        reportVersion.setComentarios("comments");
        reportVersion.setFechaImportacion(LocalDateTime.now());
        reportVersion.setFechaModificacion(LocalDateTime.now());
        reportVersions.add(reportVersion);

        when(reportVersionService.findAll()).thenReturn(reportVersions);
        when(roleVersionService.findById(any(Long.class))).thenReturn(new RoleVersion());
        when(staffingVersionService.findById(any(Long.class))).thenReturn(new StaffingVersion());
        when(certificatesService.findById(any(Long.class))).thenReturn(new CertificatesVersion());

        List<ReportVersionDto> result = reportVersionController.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("user", result.get(0).getUsuario());
        assertEquals("description", result.get(0).getDescripcion());
        assertEquals(1, result.get(0).getScreenshot());
        assertEquals("comments", result.get(0).getComentarios());
        assertEquals(reportVersion.getFechaImportacion(), result.get(0).getFechaImportacion());
        assertEquals(reportVersion.getFechaModificacion(), result.get(0).getFechaModificacion());
    }

    @Test
    void testFindAllYear() {
        String year = "2024";
        List<ReportVersion> reportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        reportVersion.setIdVersionCapacidades(1);
        reportVersion.setIdVersionStaffing(1);
        reportVersion.setIdVersionCertificaciones(1);
        reportVersion.setUsuario("user");
        reportVersion.setDescripcion("description");
        reportVersion.setScreenshot(1);
        reportVersion.setComentarios("comments");
        reportVersion.setFechaImportacion(LocalDateTime.of(2024, 1, 1, 0, 0));
        reportVersion.setFechaModificacion(LocalDateTime.of(2024, 1, 1, 0, 0));
        reportVersions.add(reportVersion);

        when(reportVersionService.findAll()).thenReturn(reportVersions);
        when(roleVersionService.findById(any(Long.class))).thenReturn(new RoleVersion());
        when(staffingVersionService.findById(any(Long.class))).thenReturn(new StaffingVersion());
        when(certificatesService.findById(any(Long.class))).thenReturn(new CertificatesVersion());

        List<ReportVersionDto> result = reportVersionController.findAllYear(year);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("user", result.get(0).getUsuario());
        assertEquals("description", result.get(0).getDescripcion());
        assertEquals(1, result.get(0).getScreenshot());
        assertEquals("comments", result.get(0).getComentarios());
        assertEquals(reportVersion.getFechaImportacion(), result.get(0).getFechaImportacion());
        assertEquals(reportVersion.getFechaModificacion(), result.get(0).getFechaModificacion());
    }

    @Test
    void testFindById() {
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);

        when(reportVersionService.findByIdVersionCapacidades(any(Long.class))).thenReturn(reportVersion);

        ReportVersion result = reportVersionController.findById("1");

        assertEquals(1L, result.getId());
    }

    @Test
    void testFindByScreenshotNum() {
        List<ReportVersion> reportVersions = new ArrayList<>();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);
        reportVersions.add(reportVersion);

        when(reportVersionService.findByScreenshot(any(String.class), any(String.class))).thenReturn(reportVersions);

        List<ReportVersion> result = reportVersionController.findByScreenshotNum("screenshot", "2024");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testFindYears() {
        List<String> years = new ArrayList<>();
        years.add("2024");

        when(reportVersionService.findYears(any(String.class))).thenReturn(years);

        List<String> result = reportVersionController.findYears("screenshot");

        assertEquals(1, result.size());
        assertEquals("2024", result.get(0));
    }

    @Test
    void testSave() {
        ReportVersionDto reportVersionDto = new ReportVersionDto();
        reportVersionDto.setId(1L);

        reportVersionController.save(1L, reportVersionDto);
    }

    @Test
    void testGenerateReport() {
        GenerateReportVersionDto generateReportVersionDto = new GenerateReportVersionDto();
        ReportVersion reportVersion = new ReportVersion();
        reportVersion.setId(1L);

        when(reportVersionService.generateReport(any(GenerateReportVersionDto.class))).thenReturn(reportVersion);

        ReportVersion result = reportVersionController.generateReport(generateReportVersionDto);

        assertEquals(1L, result.getId());
    }
}
