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

    @GetMapping("/all")
    public List<StaffingVersion> findAll(){        
        return this.staffingVersionService.findAll(); 
    }
    
    @GetMapping("/all/{year}")
    public List<StaffingVersionDto> findAllYear(@PathVariable String year){       
    	return this.staffingVersionService.findAll().stream().filter(sv->String.valueOf(sv.getFechaImportacion().getYear()).equals(year))
    			.map(sv-> { 
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
    
    @GetMapping("/{id}")
    public StaffingVersion findById(@PathVariable String id){
        return this.staffingVersionService.findById(Long.valueOf(id));     
    }
    
    @GetMapping("/years")
    public List<String> findYears(){
       return this.staffingVersionService.findYears();
    }
    
    @PutMapping({ "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody StaffingVersionDto dto) {
        this.staffingVersionService.save(id, dto);
    }
}
