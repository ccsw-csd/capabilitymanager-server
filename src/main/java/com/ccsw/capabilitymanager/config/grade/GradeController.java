package com.ccsw.capabilitymanager.config.grade;


import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccsw.capabilitymanager.config.grade.model.Grade;
import com.ccsw.capabilitymanager.config.grade.model.GradeDto;


@RequestMapping(value = "/grade")
@RestController
public class GradeController {

    @Autowired
    private GradeService gradeService;
    
    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/config")
    public List<GradeDto> findAll(){
        return this.gradeService.findAll().stream().map(g->mapper.map(g,GradeDto.class)).toList();
    }  
    
    @GetMapping("/{id}")
    public Grade findById(@PathVariable String id){
        return this.gradeService.findById(Long.valueOf(id));     
    }
}
