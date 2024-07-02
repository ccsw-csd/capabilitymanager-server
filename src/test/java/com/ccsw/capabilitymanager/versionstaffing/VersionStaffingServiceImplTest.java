package com.ccsw.capabilitymanager.versionstaffing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

public class VersionStaffingServiceImplTest {
    @Mock
    private VersionStaffingRepository versionStaffingRepository;

    @Mock
    private DataserviceS3 dataservice;

    @InjectMocks
    private VersionStaffingServiceImpl versionStaffingServiceImpl;

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
        VersionStaffing versionStaffing = new VersionStaffing();
        versionStaffing.setNombreFichero(fileName);

        GetObjectResponse response = new GetObjectResponse(null, fileName, fileName, fileName, null);

        when(versionStaffingRepository.findById(anyLong())).thenReturn(Optional.of(versionStaffing));
        when(dataservice.getMinioClient()).thenReturn(minioClient);
        when(dataservice.getBucketName()).thenReturn("nombre-bucket");

        // Act
        versionStaffingServiceImpl.recoverFileById(id, fileName);


    }

    @Test
    public void testRecoverFileById_FileNotFound() throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException {
        // Arrange
        Long id = 1L;
        String fileName = "test.txt";

        when(versionStaffingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            versionStaffingServiceImpl.recoverFileById(id, fileName);
        });
    }
}
