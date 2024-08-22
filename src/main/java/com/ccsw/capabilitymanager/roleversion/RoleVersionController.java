package com.ccsw.capabilitymanager.roleversion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersion;
import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;

@RequestMapping(value = "/roleimports")
@RestController
public class RoleVersionController {

    @Autowired
    private RoleVersionService roleVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    /**
     * Retrieves a list of all {@link RoleVersion} entities.
     * 
     * @return A list of all {@link RoleVersion} entities.
     */
    @GetMapping("/all")
    public List<RoleVersion> findAll(){        
        return this.roleVersionService.findAll(); 
    }
    
    /**
     * Retrieves a list of {@link RoleVersionDto} entities for a specific year.
     * 
     * <p>This method filters {@link RoleVersion} entities by the specified year and maps them to {@link RoleVersionDto} objects.</p>
     * 
     * @param year The year to filter the {@link RoleVersion} entities by.
     * @return A list of {@link RoleVersionDto} objects representing {@link RoleVersion} entities from the specified year.
     */
    @GetMapping("/all/{year}")
    public List<RoleVersionDto> findAllYear(@PathVariable String year){       
    	return this.roleVersionService.findAll().stream().filter(rv->String.valueOf(rv.getFechaImportacion().getYear()).equals(year))
    			//.map(rv->mapper.map(rv, RoleVersionDto.class))
    			.map(rv-> { 
	    			RoleVersionDto rvdto = new RoleVersionDto();
	    			rvdto.setIdTipoInterfaz(rv.getIdTipoInterfaz());	    			
	    			rvdto.setFechaImportacion(rv.getFechaImportacion());
	    			rvdto.setUsuario(rv.getUsuario());
	    			rvdto.setDescripcion(rv.getDescripcion());
	    			rvdto.setId(rv.getId());
	    			rvdto.setNombreFichero(rv.getNombreFichero());
	    			rvdto.setNumRegistros(rv.getNumRegistros());
	    			return rvdto;
    			})
    			.toList();
    }
    
    /**
     * Retrieves a {@link RoleVersion} entity by its ID.
     * 
     * @param id The ID of the {@link RoleVersion} entity to be retrieved.
     * @return The {@link RoleVersion} entity with the specified ID, or {@code null} if no such entity exists.
     */
    @GetMapping("/{id}")
    public RoleVersion findById(@PathVariable String id){
        return this.roleVersionService.findById(Long.valueOf(id));     
    }
    
    /**
     * Retrieves a list of years for which {@link RoleVersion} entities are available.
     * 
     * @return A list of years based on the {@link RoleVersion} entities.
     */
    @GetMapping("/years")
    public List<String> findYears(){
       return this.roleVersionService.findYears();
    }
    
    /**
     * Updates or creates a {@link RoleVersion} entity based on the provided ID and DTO.
     * 
     * <p>If an entity with the provided ID exists, it is updated with the data from the DTO. If the ID does not exist, a new entity is not created.</p>
     * 
     * @param id The ID of the {@link RoleVersion} entity to be updated. If {@code null}, a new entity is not created.
     * @param dto The {@link RoleVersionDto} containing the data to update the entity.
     */
    @PutMapping({ "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody RoleVersionDto dto) {
        this.roleVersionService.save(id, dto);
    }
}
