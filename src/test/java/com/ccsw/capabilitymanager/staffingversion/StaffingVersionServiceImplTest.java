package com.ccsw.capabilitymanager.staffingversion;

import com.ccsw.capabilitymanager.roleversion.RoleVersionRepository;
import com.ccsw.capabilitymanager.roleversion.RoleVersionServiceImpl;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;
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

public class StaffingVersionServiceImplTest {
    @Mock
    private StaffingVersionRepository staffingVersionRepository;

    @InjectMocks
    private StaffingVersionServiceImpl staffingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testSave_ExistingId() {
        // Arrange
        Long id = 1L;
        StaffingVersionDto dto = new StaffingVersionDto();
        StaffingVersion existingVersion = new StaffingVersion();
        existingVersion.setId(id);

        // Configura el comportamiento del mock para findById
        when(staffingVersionRepository.findById(id)).thenReturn(Optional.of(existingVersion));

        // Act & Assert
        staffingService.save(id,dto);
    }

    @Test
    public void testSave_NonExistingId() {
        // Arrange
        Long id = 2L; // Este ID no existe en la base de datos
        StaffingVersionDto dto = new StaffingVersionDto();

        // Configura el comportamiento del mock para findById
        when(staffingVersionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MyBadAdviceException.class, () -> {
            staffingService.save(id, dto);
        });
    }

    @Test
    void testFindAll() {
        StaffingVersion staf1 = new StaffingVersion();
        staf1.setId(1L);
        staf1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        StaffingVersion staf2 = new StaffingVersion();
        staf2.setId(2L);
        staf2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<StaffingVersion> staffingList = Arrays.asList(staf1, staf2);

        when(staffingVersionRepository.findAll()).thenReturn(staffingList);

        List<StaffingVersion> result = staffingService.findAll();

        assertEquals(2, result.size());
        assertEquals(staf2, result.get(0));
        assertEquals(staf1, result.get(1));
    }

    @Test
    void testFindYears() {
        StaffingVersion staf1 = new StaffingVersion();
        staf1.setId(1L);
        staf1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        StaffingVersion staf2 = new StaffingVersion();
        staf2.setId(2L);
        staf2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<StaffingVersion> staffingList = Arrays.asList(staf1, staf2);

        when(staffingService.findAll()).thenReturn(staffingList);

        List<String> years = staffingService.findYears();

        assertEquals(2, years.size());
        assertTrue(years.contains("2020"));
        assertTrue(years.contains("2019"));
    }
}
