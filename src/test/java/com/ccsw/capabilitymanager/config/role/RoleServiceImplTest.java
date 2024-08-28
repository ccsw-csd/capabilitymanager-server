package com.ccsw.capabilitymanager.config.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ccsw.capabilitymanager.config.role.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        role1 = new Role();
        role1.setId(1L);
        role1.setRole("Role 1");

        role2 = new Role();
        role2.setId(2L);
        role2.setRole("Role 2");
    }

    @Test
    public void testFindAll() {
        List<Role> roles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.findAll();

        assertEquals(2, result.size());
        assertEquals(role1, result.get(0));
        assertEquals(role2, result.get(1));
    }

    @Test
    public void testFindByRole() {
        String roleName = "Role 1";

        when(roleRepository.findByRole(roleName)).thenReturn(role1);

        Role result = roleService.findByRole(roleName);

        assertEquals(role1, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        when(roleRepository.findById(id)).thenReturn(Optional.of(role1));

        Role result = roleService.findById(id);

        assertEquals(role1, result);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        Role result = roleService.findById(id);

        assertNull(result);
    }
}
