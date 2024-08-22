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

	 /**
	  * Handles HTTP GET requests to retrieve a list of all certificates versions.
	  *
	  * @return A list of {@link CertificatesVersion} objects representing all certificates versions.
	  */
	   @GetMapping("/all")
	   public List<CertificatesVersion> findAll(){        
	        return this.certificatesService.findAll(); 
	    }
	   
	   /**
	    * Handles HTTP GET requests to retrieve a list of certificates versions filtered by a specific year.
	    *
	    * @param year The year used to filter the certificates versions.
	    * @return A list of {@link CertificatesVersionDto} objects representing certificates versions for the specified year.
	    */
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
	    
	    /**
	     * Handles HTTP GET requests to retrieve a certificate version by its ID.
	     *
	     * @param id The ID of the certificate version to retrieve.
	     * @return A {@link CertificatesVersion} object representing the certificate version with the specified ID.
	     * @throws NumberFormatException if the ID cannot be converted to a {@code Long}.
	     */
	    @GetMapping("/{id}")
	    public CertificatesVersion findById(@PathVariable String id){
	        return this.certificatesService.findById(Long.valueOf(id));     
	    }
	    
	    /**
	     * Handles HTTP GET requests to retrieve a list of years for which certificates versions are available.
	     *
	     * @return A list of {@link String} representing the years for which certificate versions are present.
	     */	    
	    @GetMapping("/years")
	    public List<String> findYears(){
	       return this.certificatesService.findYears();
	    }
	    
	    /**
	     * Handles HTTP PUT requests to update an existing certificate version or create a new one if the ID is not provided.
	     *
	     * @param id The ID of the certificate version to update. If {@code null}, a new certificate version will be created.
	     * @param dto The {@link CertificatesVersionDto} object containing the details to be saved or updated.
	     */	    
	    @PutMapping({ "/{id}" })
	    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody CertificatesVersionDto dto) {
	        this.certificatesService.save(id, dto);
	    }
}
