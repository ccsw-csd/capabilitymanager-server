package com.ccsw.dashboard.staffingversion;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.roleversion.model.RoleVersion;
import com.ccsw.dashboard.roleversion.model.RoleVersionDto;
import com.ccsw.dashboard.staffingversion.StaffingVersionService;
import com.ccsw.dashboard.staffingversion.model.StaffingVersion;
import com.ccsw.dashboard.staffingversion.model.StaffingVersionDto;

@RequestMapping(value = "/staffingimports")
@RestController
public class StaffingVersionController {

    @Autowired
    private StaffingVersionService staffingVersionService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<StaffingVersion> findAll(){        
        return this.staffingVersionService.findAll(); 
    }
    
    @RequestMapping(path = "/all/{year}", method = RequestMethod.GET)
    public List<StaffingVersionDto> findAllYear(@PathVariable String year){       
    	return this.staffingVersionService.findAll().stream().filter(sv->String.valueOf(sv.getFechaImportacion().getYear()).equals(year))
    			.map(sv-> { 
    				StaffingVersionDto svdto = new StaffingVersionDto();
	    			svdto.setFechaImportacion(sv.getFechaImportacion());
	    			svdto.setComentarios(sv.getComentarios());
	    			svdto.setDescripcion(sv.getDescripcion());
	    			svdto.setId(sv.getId());
	    			svdto.setNombreFichero(sv.getNombreFichero());
	    			svdto.setNumRegistros(sv.getNumRegistros());
	    			return svdto;
    			})
    			.toList();
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public StaffingVersion findById(@PathVariable String id){
        return this.staffingVersionService.findById(Long.valueOf(id));     
    }
    
    @RequestMapping(path = "/years", method = RequestMethod.GET)
    public List<String> findYears(){
       return this.staffingVersionService.findYears();
    }
    
    @RequestMapping(path = { "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody StaffingVersionDto dto) {
        this.staffingVersionService.save(id, dto);
    }
}
