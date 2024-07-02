package com.ccsw.capabilitymanager.versioncapacidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesController;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import io.minio.errors.MinioException;

public class VersionCapacidadesControllerTest {
    @Mock
    private VersionCapacidadesService versionCapacidadesService;

    @InjectMocks
    private VersionCapacidadesController versionCapacidadesController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(versionCapacidadesController).build();
    }

    @Test
    public void testGetFile() throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException {
        // Arrange
        Long id = 1L;
        String fileName = "test.txt";
        byte[] content = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);

        when(versionCapacidadesService.recoverFileById(anyLong(), anyString())).thenReturn(inputStream);

        // Act
        ResponseEntity<Resource> responseEntity = versionCapacidadesController.getFile(id, fileName);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, responseEntity.getHeaders().getContentType());
        assertNotNull(responseEntity.getBody());
    }
}
