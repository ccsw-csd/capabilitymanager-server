package com.ccsw.capabilitymanager.certificatesversion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;
public class CertificatesVersionControllerTest {
    @InjectMocks
    private CertificatesController certificatesController;

    @Mock
    private CertificatesService certificatesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        CertificatesVersion version1 = new CertificatesVersion();
        CertificatesVersion version2 = new CertificatesVersion();
        List<CertificatesVersion> versions = Arrays.asList(version1, version2);

        when(certificatesService.findAll()).thenReturn(versions);

        // Act
        List<CertificatesVersion> result = certificatesController.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(versions, result);
    }

    @Test
    public void testFindAllYear() {
        // Arrange
        CertificatesVersion version1 = new CertificatesVersion();
        version1.setFechaImportacion(LocalDateTime.of(2023, 1, 1, 0, 0));
        CertificatesVersion version2 = new CertificatesVersion();
        version2.setFechaImportacion(LocalDateTime.of(2023, 2, 1, 0, 0));
        CertificatesVersion version3 = new CertificatesVersion();
        version3.setFechaImportacion(LocalDateTime.of(2022, 1, 1, 0, 0));

        List<CertificatesVersion> versions = Arrays.asList(version1, version2, version3);
        when(certificatesService.findAll()).thenReturn(versions);

        // Act
        List<CertificatesVersionDto> result = certificatesController.findAllYear("2023");

        // Assert
        assertEquals(2, result.size());
        result.forEach(rv -> assertEquals(2023, rv.getFechaImportacion().getYear()));
    }

    @Test
    public void testFindById() {
        // Arrange
        CertificatesVersion version = new CertificatesVersion();
        version.setId(1L);
        when(certificatesService.findById(anyLong())).thenReturn(version);

        // Act
        CertificatesVersion result = certificatesController.findById("1");

        // Assert
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindYears() {
        // Arrange
        List<String> years = Arrays.asList("2022", "2023");
        when(certificatesService.findYears()).thenReturn(years);

        // Act
        List<String> result = certificatesController.findYears();

        // Assert
        assertEquals(2, result.size());
        assertEquals(years, result);
    }

    @Test
    public void testSave() {
        // Arrange
        Long id = 1L;
        CertificatesVersionDto dto = new CertificatesVersionDto();

        // Act
        certificatesController.save(id, dto);
    }
}
