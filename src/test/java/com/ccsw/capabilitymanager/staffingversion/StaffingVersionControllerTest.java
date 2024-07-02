package com.ccsw.capabilitymanager.staffingversion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

public class StaffingVersionControllerTest {
    @InjectMocks
    private StaffingVersionController staffingController;

    @Mock
    private StaffingVersionService staffingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        StaffingVersion version1 = new StaffingVersion();
        StaffingVersion version2 = new StaffingVersion();
        List<StaffingVersion> versions = Arrays.asList(version1, version2);

        when(staffingService.findAll()).thenReturn(versions);

        // Act
        List<StaffingVersion> result = staffingController.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(versions, result);
    }

    @Test
    public void testFindAllYear() {
        // Arrange
        StaffingVersion version1 = new StaffingVersion();
        version1.setFechaImportacion(LocalDateTime.of(2023, 1, 1, 0, 0));
        StaffingVersion version2 = new StaffingVersion();
        version2.setFechaImportacion(LocalDateTime.of(2023, 2, 1, 0, 0));
        StaffingVersion version3 = new StaffingVersion();
        version3.setFechaImportacion(LocalDateTime.of(2022, 1, 1, 0, 0));

        List<StaffingVersion> versions = Arrays.asList(version1, version2, version3);
        when(staffingService.findAll()).thenReturn(versions);

        // Act
        List<StaffingVersionDto> result = staffingController.findAllYear("2023");

        // Assert
        assertEquals(2, result.size());
        result.forEach(rv -> assertEquals(2023, rv.getFechaImportacion().getYear()));
    }

    @Test
    public void testFindById() {
        // Arrange
        StaffingVersion version = new StaffingVersion();
        version.setId(1L);
        when(staffingService.findById(anyLong())).thenReturn(version);

        // Act
        StaffingVersion result = staffingController.findById("1");

        // Assert
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindYears() {
        // Arrange
        List<String> years = Arrays.asList("2022", "2023");
        when(staffingService.findYears()).thenReturn(years);

        // Act
        List<String> result = staffingController.findYears();

        // Assert
        assertEquals(2, result.size());
        assertEquals(years, result);
    }

    @Test
    public void testSave() {
        // Arrange
        Long id = 1L;
        StaffingVersionDto dto = new StaffingVersionDto();

        // Act
        staffingController.save(id, dto);
    }
}
