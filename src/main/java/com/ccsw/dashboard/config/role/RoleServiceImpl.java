package com.ccsw.dashboard.config.role;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.role.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public List<Role> findAll() {
        return (List<Role>) this.roleRepository.findAll().stream().sorted().toList();
    }

    @Override
    public Role findByRole(String Role) {
        return this.roleRepository.findByRole(Role);
    }

	@Override
	public Role findById(Long id) {
		return roleRepository.findById(id).orElse(null);
	}

    
}
