package com.ccsw.capabilitymanager.versioncertificados;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

public class VersionCertificacionesServiceImplTest {
    @Mock
    private VersionCertificacionesRepository versionCertificacionesRepository;

    @Mock
    private DataserviceS3 dataservice;

    @InjectMocks
    private VersionCertificacionesServiceImpl versionCertificacionesServiceImpl;

    @Mock
    private MinioClient minioClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRecoverFileById_FileFound() throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Long id = 1L;
        String fileName = "test.txt";
        VersionCertificaciones versionCertificaciones = new VersionCertificaciones();
        versionCertificaciones.setNombreFichero(fileName);

        GetObjectResponse response = new GetObjectResponse(null, fileName, fileName, fileName, null);

        when(versionCertificacionesRepository.findById(anyLong())).thenReturn(Optional.of(versionCertificaciones));
        when(dataservice.getMinioClient()).thenReturn(minioClient);
        when(dataservice.getBucketName()).thenReturn("nombre-bucket");

        // Act
        versionCertificacionesServiceImpl.recoverFileById(id, fileName);


    }

    @Test
    public void testRecoverFileById_FileNotFound() throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Long id = 1L;
        String fileName = "test.txt";

        when(versionCertificacionesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            versionCertificacionesServiceImpl.recoverFileById(id, fileName);
        });
    }
}
