package com.ccsw.capabilitymanager.dataimport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ccsw.capabilitymanager.S3Service.s3Service;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.exception.ImportException;

public class DataImportControllerTest {

    @InjectMocks
    private DataImportController dataImportController;

    @Mock
    private DataImportService formDataImportService;

    @Mock
    private s3Service s3service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testImportData_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto responseDto = new ImportResponseDto();
        responseDto.setMessage("Data imported correctly");

        when(formDataImportService.processObject(any(ImportRequestDto.class))).thenReturn(responseDto);

        // Act
        ResponseEntity<ImportResponseDto> responseEntity = dataImportController.importData(dto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Data imported correctly", responseEntity.getBody().getMessage());
        assertEquals(null, responseEntity.getBody().getError());
    }

    @Test
    public void testImportData_ErrorDuringProcessing() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto responseDto = new ImportResponseDto();
        responseDto.setError("Error processing data");

        doThrow(new RuntimeException("Error processing file")).when(s3service).uploadFile(any(ImportRequestDto.class));

        // Act & Assert
        ImportException thrown = assertThrows(
            ImportException.class,
            () -> dataImportController.importData(dto)
        );

        // Assert
        assertEquals("Error processing file: Error processing file", thrown.getImportResponseDto().getError());
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST); // Simula la respuesta de HttpStatus
    }
    @Test
    public void testImportData_ServiceError() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto responseDto = new ImportResponseDto();
        responseDto.setError("Error processing data");

        when(formDataImportService.processObject(any(ImportRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        ImportException thrown = assertThrows(
            ImportException.class,
            () -> dataImportController.importData(dto)
        );

        // Assert
        assertEquals("Error processing data", thrown.getImportResponseDto().getError());
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST); // Simula la respuesta de HttpStatus
    }
    
}