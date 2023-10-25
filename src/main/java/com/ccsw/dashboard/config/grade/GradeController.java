package com.ccsw.dashboard.config.grade;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.grade.model.GradeDto;


@RequestMapping(value = "/grade")
@RestController
public class GradeController {

    @Autowired
    private GradeService gradeService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/config", method = RequestMethod.GET)
    public List<GradeDto> findAll(){
        return this.gradeService.findAll().stream().map(g->mapper.map(g,GradeDto.class)).toList();
    }  
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Grade findById(@PathVariable String id){
        return this.gradeService.findById(Long.valueOf(id));     
    }
}
