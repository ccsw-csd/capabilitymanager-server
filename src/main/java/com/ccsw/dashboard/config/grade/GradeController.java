package com.ccsw.dashboard.config.grade;


import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccsw.dashboard.config.BeanDozerConfig;
import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import io.jsonwebtoken.lang.Collections;

import java.util.List;
import java.util.stream.Collector;


@RequestMapping(value = "/grade")
@RestController
public class GradeController {

    @Autowired
    private GradeService gradeService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GradeDto> findAll(){
        return this.gradeService.findAll().stream().map(g->mapper.map(g,GradeDto.class)).toList();
    }  
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Grade findById(@PathVariable String id){
        return this.gradeService.findById(Long.valueOf(id));     
    }
}
