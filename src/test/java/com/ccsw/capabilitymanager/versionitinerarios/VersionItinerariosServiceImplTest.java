package com.ccsw.capabilitymanager.versionitinerarios;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

public class VersionItinerariosServiceImplTest {

    @Mock
    private VersionItinerariosRepository versionItinerariosRepository;

    @Mock
    private DataserviceS3 dataservice;

    @InjectMocks
    private VersionItinerariosServiceImpl versionItinerariosServiceImpl;

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
        VersionItinerarios versionItinerarios = new VersionItinerarios();
        versionItinerarios.setNombreFichero(fileName);
        
        GetObjectResponse response = new GetObjectResponse(null, fileName, fileName, fileName, null);

        when(versionItinerariosRepository.findById(anyLong())).thenReturn(Optional.of(versionItinerarios));
        when(dataservice.getMinioClient()).thenReturn(minioClient);
        when(dataservice.getBucketName()).thenReturn("nombre-bucket");

        // Act
        versionItinerariosServiceImpl.recoverFileById(id, fileName);


    }

    @Test
    public void testRecoverFileById_FileNotFound() throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Long id = 1L;
        String fileName = "test.txt";

        when(versionItinerariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            versionItinerariosServiceImpl.recoverFileById(id, fileName);
        });
    }


}