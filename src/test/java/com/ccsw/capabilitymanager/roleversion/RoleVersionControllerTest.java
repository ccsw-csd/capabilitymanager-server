package com.ccsw.capabilitymanager.roleversion;

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

import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;

public class RoleVersionControllerTest {
    @InjectMocks
    private RoleVersionController roleController;

    @Mock
    private RoleVersionService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        RoleVersion version1 = new RoleVersion();
        RoleVersion version2 = new RoleVersion();
        List<RoleVersion> versions = Arrays.asList(version1, version2);

        when(roleService.findAll()).thenReturn(versions);

        // Act
        List<RoleVersion> result = roleController.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(versions, result);
    }

    @Test
    public void testFindAllYear() {
        // Arrange
        RoleVersion version1 = new RoleVersion();
        version1.setFechaImportacion(LocalDateTime.of(2023, 1, 1, 0, 0));
        RoleVersion version2 = new RoleVersion();
        version2.setFechaImportacion(LocalDateTime.of(2023, 2, 1, 0, 0));
        RoleVersion version3 = new RoleVersion();
        version3.setFechaImportacion(LocalDateTime.of(2022, 1, 1, 0, 0));

        List<RoleVersion> versions = Arrays.asList(version1, version2, version3);
        when(roleService.findAll()).thenReturn(versions);

        // Act
        List<RoleVersionDto> result = roleController.findAllYear("2023");

        // Assert
        assertEquals(2, result.size());
        result.forEach(rv -> assertEquals(2023, rv.getFechaImportacion().getYear()));
    }

    @Test
    public void testFindById() {
        // Arrange
        RoleVersion version = new RoleVersion();
        version.setId(1L);
        when(roleService.findById(anyLong())).thenReturn(version);

        // Act
        RoleVersion result = roleController.findById("1");

        // Assert
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindYears() {
        // Arrange
        List<String> years = Arrays.asList("2022", "2023");
        when(roleService.findYears()).thenReturn(years);

        // Act
        List<String> result = roleController.findYears();

        // Assert
        assertEquals(2, result.size());
        assertEquals(years, result);
    }

    @Test
    public void testSave() {
        // Arrange
        Long id = 1L;
        RoleVersionDto dto = new RoleVersionDto();

        // Act
        roleController.save(id, dto);
    }
}
