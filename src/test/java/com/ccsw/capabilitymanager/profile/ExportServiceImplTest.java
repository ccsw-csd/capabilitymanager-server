package com.ccsw.capabilitymanager.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import com.ccsw.capabilitymanager.certificatesversion.CertificatesService;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.config.literal.LiteralService;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.profile.model.ProfileGroup;
import com.ccsw.capabilitymanager.profile.model.ProfileTotal;
import com.ccsw.capabilitymanager.reportversion.ReportVersionService;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersion;
import com.ccsw.capabilitymanager.reportversion.model.ReportVersionDto;
import com.ccsw.capabilitymanager.roleversion.RoleVersionService;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.staffingversion.StaffingVersionService;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

public class ExportServiceImplTest {

    @Mock
    private LiteralService literalService;

    @Mock
    private ReportVersionService reportVersionService;

    @Mock
    private StaffingVersionService staffingVersionService;

    @Mock
    private RoleVersionService roleVersionService;

    @Mock
    private CertificatesService certificatesService;

    @Mock
    private UtilsServiceImpl utilsService;

    @InjectMocks
    private ExportServiceImpl exportService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testWriteProfileTotalsToCsv() throws IOException {
        HttpServletResponse servletResponse = new MockHttpServletResponse();
        Literal literal = new Literal();
        literal.setDesc("a");
        literal.setId(1l);
        literal.setOrd(0);
        literal.setSubtype("a");
        literal.setType("a");
        List<ProfileTotal> profileTotals = new ArrayList<>();
        // Mock de literalService y otras dependencias según sea necesario
        when(literalService.findBySubtype(anyString())).thenReturn(Arrays.asList(literal));

        exportService.setProfileTotals(profileTotals);

        exportService.writeProfileTotalsToCsv("testId", servletResponse);

        // Aquí puedes agregar aserciones adicionales según el resultado esperado
    }

    @Test
    void testWriteProfileTotalsToExcel() throws IOException {
        HttpServletResponse servletResponse = new MockHttpServletResponse();
        List<ProfileTotal> profileTotals = new ArrayList<>();
        exportService.setProfileTotals(profileTotals);

        exportService.writeProfileTotalsToExcel("testId", servletResponse);

        // Aquí puedes agregar aserciones adicionales según el resultado esperado
    }

   /* @Test
    void testWriteProfileToExcel() throws IOException {
        HttpServletResponse servletResponse = new MockHttpServletResponse();
        Long idReport = 1L;
        List<ProfileGroup> profileGroups = new ArrayList<>();
        // Mock de servicios de reporte y otras dependencias según sea necesario
        ReportVersionDto reportVersionDto = new ReportVersionDto();
        reportVersionDto.setId(idReport);
      
        when(reportVersionService.findById(idReport)).thenReturn(new ReportVersion());
        when(roleVersionService.findById(any())).thenReturn(new RoleVersion());
        when(staffingVersionService.findById(any())).thenReturn(new StaffingVersion());
        when(certificatesService.findById(any())).thenReturn(new CertificatesVersion());

        exportService.setProfileGroup(profileGroups);

        exportService.writeProfileToExcel("testId", servletResponse, idReport);

        // Aquí puedes agregar aserciones adicionales según el resultado esperado
    }*/

    @Test
    void testWriteProfileToTemplateExcel() throws IOException {
        HttpServletResponse servletResponse = new MockHttpServletResponse();
        List<ProfileGroup> profileGroups = new ArrayList<>();
        exportService.setProfileGroup(profileGroups);

        exportService.writeProfileToTemplateExcel("testId", servletResponse);

        // Aquí puedes agregar aserciones adicionales según el resultado esperado
    }
}
