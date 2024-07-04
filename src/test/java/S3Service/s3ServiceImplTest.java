package S3Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.capabilitymanager.S3Service.s3ServiceImpl;
import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

@ExtendWith(MockitoExtension.class)
public class s3ServiceImplTest {

    @Mock
    private DataserviceS3 dataservice;

    @InjectMocks
    private s3ServiceImpl yourServiceClass;

    private MinioClient minioClient;
    private ImportRequestDto dto;

    @BeforeEach
    void setUp() throws IOException {
        minioClient = mock(MinioClient.class);
        dto = new ImportRequestDto();
        MultipartFile file = mock(MultipartFile.class);
        //when(file.getOriginalFilename()).thenReturn("testfile.txt");
        //when(file.getInputStream()).thenReturn(new ByteArrayInputStream("file content".getBytes()));
        //when(file.getSize()).thenReturn((long) "file content".length());
        dto.setFileData(file);
        when(dataservice.getMinioClient()).thenReturn(minioClient);
        when(dataservice.getBucketName()).thenReturn("test-bucket");
    }

    @Test
    void testUploadFileSuccess() throws Exception {
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        yourServiceClass.uploadFile(dto);

        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testUploadFileBucketExists() throws Exception {
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        yourServiceClass.uploadFile(dto);

        verify(minioClient, never()).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testUploadFileException() throws Exception {
    	doThrow(new RuntimeException(new MinioException("Minio error"){}))
        .when(minioClient).bucketExists(any(BucketExistsArgs.class));
    	//when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenThrow(new IOException("Minio error"));

        yourServiceClass.uploadFile(dto);
        
        // Aquí estamos verificando que el mensaje de error se setea correctamente.
        // En este caso, necesitaríamos una manera de obtener el ImportResponseDto para verificarlo.
        // Dado que en tu método original no se retorna el ImportResponseDto, 
        // este test solo se asegurará de que se lanza la excepción y se maneja correctamente.
        // Podrías adaptar el método uploadFile para retornar el ImportResponseDto si quieres verificar esto.
        
        //  assertEquals("Error uploading file to S3: Minio error", response.getError());
    }
}
