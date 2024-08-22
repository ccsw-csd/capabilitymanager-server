package com.ccsw.capabilitymanager.staffingversion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersion;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;

@RequestMapping(value = "/staffingimports")
@RestController
public class StaffingVersionController {

    @Autowired
    private StaffingVersionService staffingVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    /**
     * Retrieves all staffing versions.
     * 
     * @return A list of {@link StaffingVersion} objects.
     * 
     * <p>This method fetches all staffing versions from the service and returns them as a list.</p>
     */
    @GetMapping("/all")
    public List<StaffingVersion> findAll() {        
        return this.staffingVersionService.findAll(); 
    }

    /**
     * Retrieves all staffing versions for a specific year.
     * 
     * @param year The year to filter the staffing versions.
     * @return A list of {@link StaffingVersionDto} objects for the specified year.
     * 
     * <p>This method filters staffing versions by the specified year and maps them to DTOs before returning.</p>
     */
    @GetMapping("/all/{year}")
    public List<StaffingVersionDto> findAllYear(@PathVariable String year) {       
        return this.staffingVersionService.findAll().stream()
                .filter(sv -> String.valueOf(sv.getFechaImportacion().getYear()).equals(year))
                .map(sv -> { 
                    StaffingVersionDto svdto = new StaffingVersionDto();
                    svdto.setFechaImportacion(sv.getFechaImportacion());
                    svdto.setUsuario(sv.getUsuario());
                    svdto.setDescripcion(sv.getDescripcion());
                    svdto.setId(sv.getId());
                    svdto.setNombreFichero(sv.getNombreFichero());
                    svdto.setIdTipoInterfaz(sv.getIdTipoInterfaz());
                    svdto.setNumRegistros(sv.getNumRegistros());
                    return svdto;
                })
                .toList();
    }

    /**
     * Retrieves a staffing version by its ID.
     * 
     * @param id The ID of the staffing version to retrieve.
     * @return The {@link StaffingVersion} object with the specified ID.
     * 
     * <p>This method fetches a staffing version based on the provided ID. If the ID is not found, 
     * it may return null or throw an exception depending on the service implementation.</p>
     */
    @GetMapping("/{id}")
    public StaffingVersion findById(@PathVariable String id) {
        return this.staffingVersionService.findById(Long.valueOf(id));     
    }

    /**
     * Retrieves a list of years for which staffing versions are available.
     * 
     * @return A list of years as strings.
     * 
     * <p>This method retrieves all unique years from the available staffing versions.</p>
     */
    @GetMapping("/years")
    public List<String> findYears() {
        return this.staffingVersionService.findYears();
    }

    /**
     * Saves or updates a staffing version based on the provided ID.
     * 
     * @param id The ID of the staffing version to save. If the ID is null or zero, a new entity will be created.
     * @param dto The {@link StaffingVersionDto} object containing the details to be saved.
     * 
     * <p>This method saves or updates a staffing version using the provided DTO. 
     * If the ID exists, the corresponding entity is updated; otherwise, a new entity is created.</p>
     */
    @PutMapping("/{id}")
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody StaffingVersionDto dto) {
        this.staffingVersionService.save(id, dto);
    }
}
