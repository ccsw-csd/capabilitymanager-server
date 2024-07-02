package com.ccsw.capabilitymanager.roleversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;
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

public class RoleVersionServiceImplTest {
    @Mock
    private RoleVersionRepository roleVersionRepository;

    @InjectMocks
    private RoleVersionServiceImpl roleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testSave_ExistingId() {
        // Arrange
        Long id = 1L;
        RoleVersionDto dto = new RoleVersionDto();
        RoleVersion existingVersion = new RoleVersion();
        existingVersion.setId(id);

        // Configura el comportamiento del mock para findById
        when(roleVersionRepository.findById(id)).thenReturn(Optional.of(existingVersion));

        // Act & Assert
        roleService.save(id,dto);
    }

    @Test
    public void testSave_NonExistingId() {
        // Arrange
        Long id = 2L; // Este ID no existe en la base de datos
        RoleVersionDto dto = new RoleVersionDto();

        // Configura el comportamiento del mock para findById
        when(roleVersionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MyBadAdviceException.class, () -> {
            roleService.save(id, dto);
        });
    }

    @Test
    void testFindAll() {
        RoleVersion role1 = new RoleVersion();
        role1.setId(1L);
        role1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        RoleVersion role2 = new RoleVersion();
        role2.setId(2L);
        role2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<RoleVersion> roleList = Arrays.asList(role1, role2);

        when(roleVersionRepository.findAll()).thenReturn(roleList);

        List<RoleVersion> result = roleService.findAll();

        assertEquals(2, result.size());
        assertEquals(role2, result.get(0));
        assertEquals(role1, result.get(1));
    }

    @Test
    void testFindYears() {
        RoleVersion role1 = new RoleVersion();
        role1.setId(1L);
        role1.setFechaImportacion(LocalDateTime.of(2020, 1, 1, 0, 0));

        RoleVersion role2 = new RoleVersion();
        role2.setId(2L);
        role2.setFechaImportacion(LocalDateTime.of(2019, 1, 1, 0, 0));

        List<RoleVersion> roleList = Arrays.asList(role1, role2);

        when(roleService.findAll()).thenReturn(roleList);

        List<String> years = roleService.findYears();

        assertEquals(2, years.size());
        assertTrue(years.contains("2020"));
        assertTrue(years.contains("2019"));
    }
}
