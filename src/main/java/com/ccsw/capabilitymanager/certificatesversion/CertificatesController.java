package com.ccsw.capabilitymanager.certificatesversion;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersionDto;

@RestController
@RequestMapping(value = "/certificates")
public class CertificatesController {
	
	@Autowired
    private CertificatesService certificatesService;
	
	 @Autowired
     DozerBeanMapper mapper;

	   @GetMapping("/all")
	   public List<CertificatesVersion> findAll(){        
	        return this.certificatesService.findAll(); 
	    }

	    @GetMapping("/all/{year}")
	    public List<CertificatesVersionDto> findAllYear(@PathVariable String year){       
	    	return this.certificatesService.findAll().stream().filter(rv->String.valueOf(rv.getFechaImportacion().getYear()).equals(year))
	    			//.map(rv->mapper.map(rv, RoleVersionDto.class))
	    			.map(rv-> { 
	    				CertificatesVersionDto cvDto = new CertificatesVersionDto();
		    			cvDto.setIdTipoInterfaz(rv.getIdTipoInterfaz());	    			
		    			cvDto.setFechaImportacion(rv.getFechaImportacion());
		    			cvDto.setUsuario(rv.getUsuario());
		    			cvDto.setDescripcion(rv.getDescripcion());
		    			cvDto.setId(rv.getId());
		    			cvDto.setNombreFichero(rv.getNombreFichero());
		    			cvDto.setNumRegistros(rv.getNumRegistros());
		    			return cvDto;
	    			})
	    			.toList();
	    } 
	    
	    @GetMapping("/{id}")
	    public CertificatesVersion findById(@PathVariable String id){
	        return this.certificatesService.findById(Long.valueOf(id));     
	    }
	    
	    @GetMapping("/years")
	    public List<String> findYears(){
	       return this.certificatesService.findYears();
	    }
	    
	    
	    @PutMapping({ "/{id}" })
	    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody CertificatesVersionDto dto) {
	        this.certificatesService.save(id, dto);
	    }
}
