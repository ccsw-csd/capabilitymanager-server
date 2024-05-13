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

    @GetMapping("/config")
    public List<LiteralDto> findAll(){
    	 return this.literalService.findAll().stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    @GetMapping("/config/{id}")
    public List<LiteralDto> findAllByType(@PathVariable String id){
    	 return this.literalService.findByType(id).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    @GetMapping("/config/{id}/{subtype}")
    public List<LiteralDto> findAllByType(@PathVariable String id, @PathVariable String subtype){
    	 return this.literalService.findByTypeAndSubtype(id, subtype).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    } 
    
    @GetMapping("/{id}")
    public Literal findById(@PathVariable String id){
        return this.literalService.findById(Long.valueOf(id));     
    }
}
