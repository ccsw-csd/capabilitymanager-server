package com.ccsw.capabilitymanager.config.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.config.role.model.Role;
import com.ccsw.capabilitymanager.config.role.model.RoleDto;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleControllerTest {
    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @Mock
    private DozerBeanMapper mapper;

    private Role role1;
    private Role role2;
    private RoleDto roleDto1;
    private RoleDto roleDto2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        role1 = new Role();
        role1.setId(1L);

        role2 = new Role();
        role2.setId(2L);

        roleDto1 = new RoleDto();
        roleDto1.setId(1);

        roleDto2 = new RoleDto();
        roleDto2.setId(2);
    }

    @Test
    public void testFindAll() {
        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.findAll()).thenReturn(roles);
        when(mapper.map(role1, RoleDto.class)).thenReturn(roleDto1);
        when(mapper.map(role2, RoleDto.class)).thenReturn(roleDto2);

        List<RoleDto> result = roleController.findAll();

        assertEquals(2, result.size());
        assertEquals(roleDto1, result.get(0));
        assertEquals(roleDto2, result.get(1));
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        when(roleService.findById(id)).thenReturn(role1);

        Role result = roleController.findById(id.toString());

        assertEquals(role1, result);
    }
}
