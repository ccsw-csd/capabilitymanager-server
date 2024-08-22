package com.ccsw.capabilitymanager.config.role;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.config.role.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;
    
    /**
     * Retrieves a list of all roles, sorted in their natural order.
     *
     * @return A list of {@link Role} objects representing all roles, sorted by their natural ordering.
     */    
    @Override
    public List<Role> findAll() {
        return (List<Role>) this.roleRepository.findAll().stream().sorted().toList();
    }
    
    /**
     * Retrieves a role by its name.
     *
     * @param role The name of the role to retrieve.
     * @return The {@link Role} object with the specified name, or {@code null} if no role with the given name exists.
     */
    @Override
    public Role findByRole(String Role) {
        return this.roleRepository.findByRole(Role);
    }
    
    /**
     * Retrieves a role by its ID.
     *
     * @param id The ID of the role to retrieve.
     * @return The {@link Role} object with the specified ID, or {@code null} if no role with the given ID exists.
     */
	@Override
	public Role findById(Long id) {
		return roleRepository.findById(id).orElse(null);
	}

    
}
