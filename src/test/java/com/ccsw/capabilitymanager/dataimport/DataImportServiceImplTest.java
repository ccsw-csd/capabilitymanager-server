package com.ccsw.capabilitymanager.dataimport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.capabilitymanager.S3Service.s3Service;
import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapatidadesRepository;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versionitinerarios.VersionItinerariosRepository;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;

public class DataImportServiceImplTest {

    @InjectMocks
    private DataImportServiceImpl dataImportService;

    @Mock
    private FormDataImportRepository formDataImportRepository;

    @Mock
    private DataserviceS3 dataservice;

    @Mock
    private s3Service s3service;

    @Mock
    private UtilsServiceImpl utilsServiceImpl;

    @Mock
    private VersionCapatidadesRepository versionCapatidadesRepository;

    @Mock
    private VersionStaffingRepository versionStaffingRepository;

    @Mock
    private VersionItinerariosRepository versionItinerariosRepository;

    @Mock
    private VersionCertificacionesRepository versionCertificacionesRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessObject_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        expectedResponse.setMessage("Data imported correctly");

        when(dto.getDocumentType()).thenReturn("2");

        // Act
        ImportResponseDto actualResponse = dataImportService.processObject(dto);

        // Assert
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getError(), actualResponse.getError());
    }

    @Test
    public void testProcessObject_Error() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        expectedResponse.setError("Error processing data");

        when(dto.getDocumentType()).thenReturn("invalidType");

        // Act
        ImportResponseDto actualResponse = dataImportService.processObject(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus().value());
        assertEquals("Error processing data", actualResponse.getError());
    }

    @Test
    public void testProcessRolsDoc_Catch() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);

        when(dto.getDocumentType()).thenReturn("2");
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(2);
        //when(utilsServiceImpl.getStringValue(any(), any())).thenReturn("test");

        // Act
        ImportResponseDto actualResponse = dataImportService.processObject(dto);

    }
    
    @Test
    public void testProcessRolsDoc_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);
        Row mockRow1 = mock(Row.class);
        Row mockRow2 = mock(Row.class);
        
        MultipartFile file = mock(MultipartFile.class);

        when(dto.getDocumentType()).thenReturn("2");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(3); // Simulando 2 filas de datos más la fila de encabezado

        // Mockear las filas retornadas
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_START)).thenReturn(mockRow1);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT)).thenReturn(mockRow2);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT + 1)).thenReturn(null); // Termina el bucle
        
        //when(utilsServiceImpl.getStringValue(any(), any())).thenReturn("test");

        // Act
        ImportResponseDto actualResponse = dataImportService.processObject(dto);

        // Assert
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getError(), actualResponse.getError());
    }
    
    @Test
    public void testProcessRolsDoc_Exception() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);
        
        MultipartFile file = mock(MultipartFile.class);

        when(dto.getDocumentType()).thenReturn("2");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(2);
        //when(utilsServiceImpl.getStringValue(any(), any())).thenReturn("test");

        // Act & Assert
        assertThrows(UnprocessableEntityException.class, () -> {
            dataImportService.processObject(dto);
        });

    }

    @Test
    public void testProcessRolsDoc_Error() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);

        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(0);

        // Act and Assert
        try {
            dataImportService.processObject(dto);
        } catch (UnprocessableEntityException e) {
            assertEquals(Constants.ERROR_EMPTY_ROL_FILE, e.getMessage());
        }
    }
}