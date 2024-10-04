package com.ccsw.capabilitymanager.dataimport;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.capabilitymanager.versioncapacidades.VersionCapacidadesRepository;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;
import com.ccsw.capabilitymanager.versionstaffing.VersionStaffingRepository;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

@ExtendWith(MockitoExtension.class)
public class ErrorServiceImplTest {

    @Mock
    private VersionCapacidadesRepository versionCapacidadesRepository;

    @Mock
    private VersionStaffingRepository versionStaffingRepository;

    @InjectMocks
    private ErrorServiceImpl errorService;

    private VersionCapacidades versionCapacidades;
    private VersionStaffing versionStaffing;

    @BeforeEach
    void setUp() {
        versionCapacidades = new VersionCapacidades();
        versionStaffing = new VersionStaffing();
    }

    @Test
    void testStaffingError() {
        // Act
        errorService.staffingError(versionStaffing);

        // Assert
        verify(versionStaffingRepository, times(1)).save(versionStaffing);
    }

    @Test
    void testFormError() {
        // Act
        errorService.formError(versionCapacidades);

        // Assert
        verify(versionCapacidadesRepository, times(1)).save(versionCapacidades);
    }
}