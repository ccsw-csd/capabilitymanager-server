package com.ccsw.capabilitymanager.roleversion;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@Transactional
public class RoleVersionServiceImpl implements RoleVersionService{
	private static final String ERROR_INIT = ">>> [ERROR][RoleVersionServiceImpl] (";
    @Autowired
    private RoleVersionRepository RoleVersionRepository;
    
    /**
     * Retrieves a list of all {@link RoleVersion} entities, sorted by their natural ordering.
     * 
     * @return A list of all {@link RoleVersion} entities.
     */
    @Override
    public List<RoleVersion> findAll() {
        return (List<RoleVersion>) this.RoleVersionRepository.findAll().stream().sorted().toList();
    }
      
    /**
     * Retrieves a {@link RoleVersion} entity by its ID.
     * 
     * @param id The ID of the {@link RoleVersion} entity to be retrieved.
     * @return The {@link RoleVersion} entity with the specified ID, or {@code null} if no such entity exists.
     */
    @Override
    public RoleVersion findById(Long id) {
        return this.RoleVersionRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a list of unique years based on the {@link FechaImportacion} of {@link RoleVersion} entities.
     * 
     * <p>This method collects years from the {@link FechaImportacion} field of all {@link RoleVersion} entities and returns them as a list of unique year strings.</p>
     * 
     * @return A list of unique years extracted from the {@link FechaImportacion} field of {@link RoleVersion} entities.
     */
	@Override
	public List<String> findYears() {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<RoleVersion> listRoleVersion = findAll();
		for (RoleVersion roleVersion : listRoleVersion) {
			String year = String.valueOf(roleVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");			
		}
		
		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());			
		}	
		
		return rvList;
	}

	/**
	 * Updates an existing {@link RoleVersion} entity with data from the provided DTO.
	 * 
	 * <p>Updates the {@link RoleVersion} entity identified by the specified ID with values from the {@link RoleVersionDto} object. If the entity with the given ID does not exist, an exception is thrown.</p>
	 * 
	 * @param id The ID of the {@link RoleVersion} entity to be updated.
	 * @param dto The {@link RoleVersionDto} containing the data to update the entity.
	 * @throws MyBadAdviceException if the {@link RoleVersion} entity with the given ID does not exist.
	 */
	@Override
	public void save(Long id, RoleVersionDto dto) {
		RoleVersion roleVersion;      
        roleVersion = this.findById(id);       
        if (roleVersion == null) {
			CapabilityLogger.logError(ERROR_INIT + "save) : Error al guardar RoleVersion el id no existe.");
			throw new MyBadAdviceException("roleVersion id doesn't exist");
		}
        BeanUtils.copyProperties(dto, roleVersion, "id");
        //roleVersion.setFechaimportacion(dto.getFechaImportacion());
        this.RoleVersionRepository.save(roleVersion);
		
	}
	
	
    
}
