package com.ccsw.dashboard.config.literal;


import java.util.List;
import java.util.stream.Stream;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.config.literal.model.Literal;
import com.ccsw.dashboard.config.literal.model.LiteralDto;



@RequestMapping(value = "/literal")
@RestController
public class LiteralController {

    @Autowired
    private LiteralService literalService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/config", method = RequestMethod.GET)
    public List<LiteralDto> findAll(){
    	 return this.literalService.findAll().stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    @RequestMapping(path = "/config/{id}", method = RequestMethod.GET)
    public List<LiteralDto> findAllByType(@PathVariable String id){
    	 return this.literalService.findByType(id).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    }
    
    @RequestMapping(path = "/config/{id}/{subtype}", method = RequestMethod.GET)
    public List<LiteralDto> findAllByType(@PathVariable String id, @PathVariable String subtype){
    	 return this.literalService.findByTypeAndSubtype(id, subtype).stream().map(l->mapper.map(l,LiteralDto.class)).toList();
    } 
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Literal findById(@PathVariable String id){
        return this.literalService.findById(Long.valueOf(id));     
    }
}
