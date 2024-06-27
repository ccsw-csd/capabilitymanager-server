package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.errors.MinioException;



@RequestMapping(value = "/mantenimiento/itinerariosFormativos")
@RestController
public class MatenimientoItinerariosFormativosController {
	private static final Logger logger = LoggerFactory.getLogger(MatenimientoItinerariosFormativosController.class);

	@Autowired
	private MantenimientoItinerariosFormativosService mantenimientoIntinerariosFormativosService;


    @GetMapping(path = "/showAll")
    public List<ItinerariosFormativos> getAll() throws InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	logger.debug("Prueba");
    	return this.mantenimientoIntinerariosFormativosService.findAll(); 
    	
    					
    }    
    
    @PostMapping(path = "/insert")
    public void setItinerarioFormativo(@RequestBody ItinerariosFormativosDto dto) throws ParseException{
    	logger.debug("insert");
    
    	mantenimientoIntinerariosFormativosService.save(dto);
    }   
    
    @PutMapping(path = "/update")
    public void updateItinerarioFormativo(@RequestBody ItinerariosFormativosDto dto) throws ParseException{
    	logger.debug("Update");
    
    	mantenimientoIntinerariosFormativosService.update(dto);
    }  
    
    @DeleteMapping(path = "delete/{id}")
    public void eliminarItinerarioFormativo(@PathVariable(name = "id", required = false) Long id) throws ParseException{
    	logger.debug("Delete");
    
    	mantenimientoIntinerariosFormativosService.delete(id);
    }  
}
