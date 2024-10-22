package com.ccsw.capabilitymanager.dataimport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ccsw.capabilitymanager.activity.ActivityRepository;
import com.ccsw.capabilitymanager.activitydataimport.ActivityDataImportRepository;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.websocket.WebSocketService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.capabilitymanager.fileprocess.S3Service;
import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesActividadDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.CertificatesDataImportRepository;
import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataImport;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.formdataimport.FormDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.ItinerariosActividadDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.ItinerariosDataImportRepository;
import com.ccsw.capabilitymanager.itinerariosdataimport.model.ItinerariosDataImport;
import com.ccsw.capabilitymanager.staffingdataimport.StaffingDataImportRepository;
import com.ccsw.capabilitymanager.staffingdataimport.model.StaffingDataImport;
import com.ccsw.capabilitymanager.utils.UtilsServiceImpl;
import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versioncertificados.VersionCertificacionesRepository;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;
import com.ccsw.capabilitymanager.versionitinerarios.VersionItinerariosRepository;
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

public class DataImportServiceImplTest {

    @InjectMocks
    private DataImportServiceImpl dataImportService;

    @Mock
    private FormDataImportRepository formDataImportRepository;

    @Mock
    private DataserviceS3 dataservice;

    @Mock
    private S3Service s3service;

    @Mock
    private UtilsServiceImpl utilsServiceImpl;

    @Mock
    private VersionCapacidadesRepository versionCapacidadesRepository;

    @Mock
    private VersionStaffingRepository versionStaffingRepository;

    @Mock
    private VersionItinerariosRepository versionItinerariosRepository;

    @Mock
    private VersionCertificacionesRepository versionCertificacionesRepository;
    
    @Mock
	private StaffingDataImportRepository staffingDataImportRepository;
    
    @Mock
	private CertificatesDataImportRepository certificatesDataImportRepository;
    
    @Mock
	private CertificatesActividadDataImportRepository certificatesActividadDataImportRepository;
	
    @Mock
	private ItinerariosDataImportRepository itinerariosDataImportRepository;
    
    @Mock
	private ItinerariosActividadDataImportRepository itinerariosActividadDataImportRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityDataImportRepository actividadDataImportRepository;

    @Mock
    private WebSocketService webSocketService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
        VersionCapacidades versionCap = new VersionCapacidades();
        MultipartFile file = mock(MultipartFile.class);

        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_START)).thenReturn(mockRow1);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT)).thenReturn(mockRow2);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT + 1)).thenReturn(null); // End loop

        // Mock cell values for each row
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.RolsDatabasePos.COL_SAGA.getPosition())).thenReturn("saga1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.RolsDatabasePos.COL_EMAIL.getPosition())).thenReturn("email1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.RolsDatabasePos.COL_NAME.getPosition())).thenReturn("name1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition())).thenReturn("Software Engineer(blablabla)");
        // Add other necessary mock values for mockRow1

        when(utilsServiceImpl.getStringValue(mockRow2, Constants.RolsDatabasePos.COL_SAGA.getPosition())).thenReturn("saga2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.RolsDatabasePos.COL_EMAIL.getPosition())).thenReturn("email2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.RolsDatabasePos.COL_NAME.getPosition())).thenReturn("name2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.RolsDatabasePos.COL_ROLL1_EXTENDIDO.getPosition())).thenReturn("Engagement Managers(blablabla)");
       
        when(dto.getDocumentType()).thenReturn("2");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(3); // Simulando 2 filas de datos más la fila de encabezado
        when(versionCapacidadesRepository.save(Mockito.any())).thenReturn(versionCap);
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
    public void testProcessStaffingsDoc_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);
        Row mockRow1 = mock(Row.class);
        Row mockRow2 = mock(Row.class);
        VersionStaffing versionStaf = new VersionStaffing();
        MultipartFile file = mock(MultipartFile.class);

        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_START)).thenReturn(mockRow1);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT)).thenReturn(mockRow2);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT + 1)).thenReturn(null); // End loop

        // Mock cell values for each row
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.StaffingDatabasePos.COL_SAGA.getPosition())).thenReturn("saga1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.StaffingDatabasePos.COL_GGID.getPosition())).thenReturn("email1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.StaffingDatabasePos.COL_CENTRO.getPosition())).thenReturn("name1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.StaffingDatabasePos.COL_NOMBRE.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.StaffingDatabasePos.COL_PRACTICE_AREA.getPosition())).thenReturn("Ultimo campo");
        // Add other necessary mock values for mockRow1

        when(utilsServiceImpl.getStringValue(mockRow2, Constants.StaffingDatabasePos.COL_SAGA.getPosition())).thenReturn("saga2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.StaffingDatabasePos.COL_GGID.getPosition())).thenReturn("email2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.StaffingDatabasePos.COL_CENTRO.getPosition())).thenReturn("name2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.StaffingDatabasePos.COL_NOMBRE.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.StaffingDatabasePos.COL_PRACTICE_AREA.getPosition())).thenReturn("Ultimo campo");
       
        when(dto.getDocumentType()).thenReturn("1");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        List<StaffingDataImport> staffingDataImportList = new ArrayList<>();
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(3); // Simulando 2 filas de datos más la fila de encabezado
        when(versionStaffingRepository.save(Mockito.any())).thenReturn(versionStaf);
        when(staffingDataImportRepository.saveAll(Mockito.any())).thenReturn(staffingDataImportList);
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
    public void testProcessCertificatesDoc_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);
        Row mockRow1 = mock(Row.class);
        Row mockRow2 = mock(Row.class);
        VersionCertificaciones versionCer = new VersionCertificaciones();
        MultipartFile file = mock(MultipartFile.class);

        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_START)).thenReturn(mockRow1);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT)).thenReturn(mockRow2);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT + 1)).thenReturn(null); // End loop
        String dateString = "2000-12-31";
        // Crear un formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dia = dateFormat.parse(dateString);

        // Mock cell values for each row
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_SAGA.getPosition())).thenReturn("saga1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_PARTNER.getPosition())).thenReturn("email1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_CODE.getPosition())).thenReturn("code");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_CERTIFICADO.getPosition())).thenReturn("name1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_NAME_GTD.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getDateValue(mockRow1, Constants.CertificatesDatabasePos.COL_FECHA_CERTIFICADO.getPosition())).thenReturn(dia);
        when(utilsServiceImpl.getDateValue(mockRow1, Constants.CertificatesDatabasePos.COL_FECHA_EXPIRACION.getPosition())).thenReturn(dia);
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.CertificatesDatabasePos.COL_CERTIFICATION_GTD.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getDateValue(mockRow1, Constants.CertificatesDatabasePos.COL_ANEXO.getPosition())).thenReturn(dia);
        
        // Add other necessary mock values for mockRow1

        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_SAGA.getPosition())).thenReturn("saga2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_PARTNER.getPosition())).thenReturn("email2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_CODE.getPosition())).thenReturn("code");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_CERTIFICADO.getPosition())).thenReturn("name2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_NAME_GTD.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getDateValue(mockRow2, Constants.CertificatesDatabasePos.COL_FECHA_CERTIFICADO.getPosition())).thenReturn(dia);
        when(utilsServiceImpl.getDateValue(mockRow1, Constants.CertificatesDatabasePos.COL_FECHA_EXPIRACION.getPosition())).thenReturn(dia);
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.CertificatesDatabasePos.COL_CERTIFICATION_GTD.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getDateValue(mockRow2, Constants.CertificatesDatabasePos.COL_ANEXO.getPosition())).thenReturn(dia);
        
        when(dto.getDocumentType()).thenReturn("3");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        List<CertificatesDataImport> certificatesDataImport = new ArrayList<>();
        List<ActivityDataImport> actividadDataImport = new ArrayList<>();
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(3); // Simulando 2 filas de datos más la fila de encabezado
        when(versionCertificacionesRepository.save(Mockito.any())).thenReturn(versionCer);
        when(certificatesDataImportRepository.saveAll(Mockito.any())).thenReturn(certificatesDataImport);
        when(actividadDataImportRepository.saveAll(Mockito.any())).thenReturn(actividadDataImport);
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
    public void testProcessItinerariosDoc_Success() throws Exception {
        // Arrange
        ImportRequestDto dto = mock(ImportRequestDto.class);
        ImportResponseDto expectedResponse = new ImportResponseDto();
        Sheet mockSheet = mock(Sheet.class);
        Row mockRow1 = mock(Row.class);
        Row mockRow2 = mock(Row.class);
        VersionItinerarios versionItin = new VersionItinerarios();
        MultipartFile file = mock(MultipartFile.class);

        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_START)).thenReturn(mockRow1);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT)).thenReturn(mockRow2);
        when(mockSheet.getRow(Constants.ROW_EVIDENCE_LIST_NEXT + 1)).thenReturn(null); // End loop

        // Mock cell values for each row
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_GGID.getPosition())).thenReturn("saga1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_FIRST_NAME.getPosition())).thenReturn("email1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_LAST_NAME.getPosition())).thenReturn("name1");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_EMAIL_ID.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_COMPLETION_PERCENT.getPosition())).thenReturn("31.25%");
        when(utilsServiceImpl.getStringValue(mockRow1, Constants.ItinerariosDatabasePos.COL_PATHWAY_ID.getPosition())).thenReturn("2");

        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_GGID.getPosition())).thenReturn("saga2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_FIRST_NAME.getPosition())).thenReturn("email2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_LAST_NAME.getPosition())).thenReturn("name2");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_EMAIL_ID.getPosition())).thenReturn("Software Engineer");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_COMPLETION_PERCENT.getPosition())).thenReturn("31.25%");
        when(utilsServiceImpl.getStringValue(mockRow2, Constants.ItinerariosDatabasePos.COL_PATHWAY_ID.getPosition())).thenReturn("2");
        
        
        when(dto.getDocumentType()).thenReturn("4");
        when(dto.getDescription()).thenReturn("des");
        when(dto.getUser()).thenReturn("User");
        when(dto.getFileData()).thenReturn(file);
        
        List<ItinerariosDataImport> itinerariosDataImport = new ArrayList<>();
        List<ActivityDataImport> actividadDataImport = new ArrayList<>();
        
        when(utilsServiceImpl.obtainSheet(any())).thenReturn(mockSheet);
        when(mockSheet.getPhysicalNumberOfRows()).thenReturn(3); // Simulando 2 filas de datos más la fila de encabezado
        when(versionStaffingRepository.save(Mockito.any())).thenReturn(versionItin);
        when(itinerariosDataImportRepository.saveAll(Mockito.any())).thenReturn(itinerariosDataImport);
        when(actividadDataImportRepository.saveAll(Mockito.any())).thenReturn(actividadDataImport);
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


}