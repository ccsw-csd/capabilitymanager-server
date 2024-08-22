package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
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
	@Autowired
	private MantenimientoItinerariosFormativosService mantenimientoIntinerariosFormativosService;


	/**
	 * Retrieves a list of all {@link ItinerariosFormativos} entities.
	 *
	 * <p>This method fetches all entities of type {@link ItinerariosFormativos} from the service layer. It may throw several
	 * exceptions related to data handling and external service interactions.</p>
	 *
	 * @return A list of all {@link ItinerariosFormativos} entities.
	 * @throws InvalidKeyException If an invalid key is encountered during the process.
	 * @throws java.security.InvalidKeyException If an invalid cryptographic key is encountered.
	 * @throws NoSuchAlgorithmException If a specified algorithm is not available.
	 * @throws IllegalArgumentException If an illegal argument is passed to a method.
	 * @throws IOException If an I/O error occurs.
	 * @throws MinioException If there is an error interacting with MinIO storage service.
	 */
    @GetMapping(path = "/showAll")
    public List<ItinerariosFormativos> getAll() throws InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	CapabilityLogger.logDebug("Prueba");
    	return this.mantenimientoIntinerariosFormativosService.findAll(); 
    	
    					
    }    
    
    /**
     * Inserts a new {@link ItinerariosFormativos} entity using the provided DTO.
     *
     * <p>This method receives a {@link ItinerariosFormativosDto} object, logs a debug message, and then saves the corresponding
     * {@link ItinerariosFormativos} entity using the service layer.</p>
     *
     * @param dto The {@link ItinerariosFormativosDto} object containing the data to be inserted.
     * @throws ParseException If there is an error parsing date values in the DTO.
     */
    @PostMapping(path = "/insert")
    public void setItinerarioFormativo(@RequestBody ItinerariosFormativosDto dto) throws ParseException{
        CapabilityLogger.logDebug("insert");
    
    	mantenimientoIntinerariosFormativosService.save(dto);
    }   
    
    /**
     * Updates an existing {@link ItinerariosFormativos} entity with the data provided in the DTO.
     *
     * <p>This method receives an {@link ItinerariosFormativosDto} object, logs a debug message, and then updates the corresponding
     * {@link ItinerariosFormativos} entity using the service layer.</p>
     *
     * @param dto The {@link ItinerariosFormativosDto} object containing the data to update.
     * @throws ParseException If there is an error parsing date values in the DTO.
     */
    @PutMapping(path = "/update")
    public void updateItinerarioFormativo(@RequestBody ItinerariosFormativosDto dto) throws ParseException{
        CapabilityLogger.logDebug("Update");
    
    	mantenimientoIntinerariosFormativosService.update(dto);
    }  
    
    /**
     * Deletes an {@link ItinerariosFormativos} entity by its ID.
     *
     * <p>This method receives an ID as a path variable, logs a debug message, and then deletes the corresponding
     * {@link ItinerariosFormativos} entity using the service layer.</p>
     *
     * @param id The ID of the {@link ItinerariosFormativos} entity to be deleted.
     * @throws ParseException If there is an error related to parsing during the deletion process.
     */
    @DeleteMapping(path = "delete/{id}")
    public void eliminarItinerarioFormativo(@PathVariable(name = "id", required = false) Long id) throws ParseException{
        CapabilityLogger.logDebug("Delete");
    
    	mantenimientoIntinerariosFormativosService.delete(id);
    }  
}
