package com.ccsw.capabilitymanager.config.literal;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.config.literal.model.LiteralDto;


@RequestMapping(value = "/literal")
@RestController
public class LiteralController {

    @Autowired
    private LiteralService literalService;
    
    @Autowired
    DozerBeanMapper mapper;

    /**
     * Handles HTTP GET requests to retrieve a list of all literals.
     *
     * @return A list of {@link LiteralDto} objects representing all literals.
     */
    @GetMapping("/config")
    public List<LiteralDto> findAll(){
    	 return this.literalService.findAll().stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    /**
     * Handles HTTP GET requests to retrieve a list of literals filtered by type.
     *
     * @param id The type identifier used to filter the literals.
     * @return A list of {@link LiteralDto} objects representing literals of the specified type.
     */
    @GetMapping("/config/{id}")
    public List<LiteralDto> findAllByType(@PathVariable String id){
    	 return this.literalService.findByType(id).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    /**
     * Handles HTTP GET requests to retrieve a list of literals filtered by type and subtype.
     *
     * @param id The type identifier used to filter the literals.
     * @param subtype The subtype identifier used to further filter the literals.
     * @return A list of {@link LiteralDto} objects representing literals of the specified type and subtype.
     */
    @GetMapping("/config/{id}/{subtype}")
    public List<LiteralDto> findAllByType(@PathVariable String id, @PathVariable String subtype){
    	 return this.literalService.findByTypeAndSubtype(id, subtype).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    } 
    
    /**
     * Handles HTTP GET requests to retrieve a literal by its ID.
     *
     * @param id The ID of the literal to retrieve.
     * @return The {@link Literal} object with the specified ID, or {@code null} if no literal with the given ID exists.
     * @throws NumberFormatException If the ID cannot be converted to a {@code Long}.
     */  
    @GetMapping("/{id}")
    public Literal findById(@PathVariable String id){
        return this.literalService.findById(Long.valueOf(id));     
    }
}
