package com.ccsw.capabilitymanager.certificatesversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;
import com.ccsw.capabilitymanager.exception.MyBadAdviceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.anyLong;


public class CertificatesVersionServiceImplTest {

    @Mock
    private CertificatesVersionRepository certificatesVersionRepository;

    @InjectMocks
    private CertificatesServiceImpl certificatesService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testSave_ExistingId() {
        // Arrange
        Long id = 1L;
        CertificatesVersionDto dto = new CertificatesVersionDto();
        CertificatesVersion existingVersion = new CertificatesVersion();
        existingVersion.setId(id);

        // Configura el comportamiento del mock para findById
        when(certificatesVersionRepository.findById(id)).thenReturn(Optional.of(existingVersion));

        // Act & Assert
        certificatesService.save(id,dto);
    }

    @Test
    public void testSave_NonExistingId() {
        // Arrange
        Long id = 2L; // Este ID no existe en la base de datos
        CertificatesVersionDto dto = new CertificatesVersionDto();

        // Configura el comportamiento del mock para findById
        when(certificatesVersionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MyBadAdviceException.class, () -> {
            certificatesService.save(id, dto);
        });
    }

    @Test
    void testFindAll() {
        CertificatesVersion cert1 = new CertificatesVersion();
        cert1.setId(1L);
        cert1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        CertificatesVersion cert2 = new CertificatesVersion();
        cert2.setId(2L);
        cert2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<CertificatesVersion> certList = Arrays.asList(cert1, cert2);

        when(certificatesVersionRepository.findAll()).thenReturn(certList);

        List<CertificatesVersion> result = certificatesService.findAll();

        assertEquals(2, result.size());
        assertEquals(cert2, result.get(0));
        assertEquals(cert1, result.get(1));
    }

    @Test
    void testFindYears() {
        CertificatesVersion cert1 = new CertificatesVersion();
        cert1.setId(1L);
        cert1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        CertificatesVersion cert2 = new CertificatesVersion();
        cert2.setId(2L);
        cert2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<CertificatesVersion> certList = Arrays.asList(cert1, cert2);

        when(certificatesService.findAll()).thenReturn(certList);

        List<String> years = certificatesService.findYears();

        assertEquals(2, years.size());
        assertTrue(years.contains("2020"));
        assertTrue(years.contains("2019"));
    }
}
