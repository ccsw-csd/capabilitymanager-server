package com.ccsw.capabilitymanager.staffingversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.exception.MyBadAdviceException;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StaffingVersionServiceImpl implements StaffingVersionService{
	private static final String ERROR_INIT = ">>> [ERROR][StaffingVersionServiceImpl] (";
    @Autowired
    private StaffingVersionRepository StaffingVersionRepository;
    
    
    /**
     * Retrieves all staffing versions.
     * 
     * @return A list of {@link StaffingVersion} objects.
     * 
     * <p>This method fetches all staffing versions from the repository, sorts them, and returns them as a list.</p>
     */
    @Override
    public List<StaffingVersion> findAll() {
        return (List<StaffingVersion>) this.StaffingVersionRepository.findAll().stream().sorted().toList();
    }
  
    /**
     * Retrieves a staffing version by its ID.
     * 
     * @param id The ID of the staffing version to retrieve.
     * @return The {@link StaffingVersion} object with the specified ID, or null if not found.
     * 
     * <p>This method fetches a staffing version based on the provided ID. If the ID is not found, 
     * it returns null.</p>
     */
    @Override
    public StaffingVersion findById(Long id) {
        return this.StaffingVersionRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a list of years for which staffing versions are available.
     * 
     * @return A list of years as strings.
     * 
     * <p>This method collects all unique years from the available staffing versions and returns them.</p>
     */
	@Override
	public List<String> findYears() {
		List<String> rvList = new ArrayList<String>();
		Map<String, String> rvMap = new HashMap<String, String>();
		List<StaffingVersion> listStaffingVersion = findAll();
		for (StaffingVersion staffingVersion : listStaffingVersion) {
			String year = String.valueOf(staffingVersion.getFechaImportacion().getYear());
			rvMap.putIfAbsent(year, "");			
		}
		
		for (Entry<String, String> entry : rvMap.entrySet()) {
			rvList.add(entry.getKey());			
		}	
		
		return rvList;
	}

	   /**
     * Saves or updates a staffing version based on the provided ID.
     * 
     * @param id The ID of the staffing version to save. If the ID is null or zero, 
     *           a new entity will be created.
     * @param dto The {@link StaffingVersionDto} object containing the details to be saved.
     * 
     * <p>This method updates an existing staffing version if the ID is valid; otherwise, it throws an exception. 
     * It copies properties from the DTO to the entity and saves it to the repository.</p>
     * 
     * @throws MyBadAdviceException if the staffing version with the provided ID does not exist.
     */
	@Override
	public void save(Long id, StaffingVersionDto dto) {
		StaffingVersion staffingVersion;      
        staffingVersion = this.findById(id);       
        if (staffingVersion == null) {
			CapabilityLogger.logError(ERROR_INIT + "save) : Error al guardar StaffingVersion el id:"+ id +" no existe.");
			throw new MyBadAdviceException("staffingVersion id doesn't exist");
		}
        BeanUtils.copyProperties(dto, staffingVersion, "id");
        //roleVersion.setFechaimportacion(dto.getFechaImportacion());
        this.StaffingVersionRepository.save(staffingVersion);
		
	}

	    
}
