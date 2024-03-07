package com.ccsw.dashboard.roleversion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;

@RequestMapping(value = "/roleimports")
@RestController
public class RoleVersionController {

    @Autowired
    private RoleVersionService roleVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/all")
    public List<RoleVersion> findAll(){        
        return this.roleVersionService.findAll(); 
    }
    
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
    
    @GetMapping("/{id}")
    public RoleVersion findById(@PathVariable String id){
        return this.roleVersionService.findById(Long.valueOf(id));     
    }
    
    @GetMapping("/years")
    public List<String> findYears(){
       return this.roleVersionService.findYears();
    }
    
    @PutMapping({ "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody RoleVersionDto dto) {
        this.roleVersionService.save(id, dto);
    }
}
