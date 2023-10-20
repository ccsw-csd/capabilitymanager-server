package com.ccsw.dashboard.config.grade;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.grade.model.Grade;

import java.util.List;

@Service
@Transactional
public class GradeServiceImpl implements GradeService{

    @Autowired
    private GradeRepository gradeRepository;
    
    @Override
    public List<Grade> findAll() {
        return (List<Grade>) this.gradeRepository.findAll();
    }

    @Override
    public Grade findByGrade(String grade) {
        return this.gradeRepository.findByGrade(grade);
    }
    
    @Override
    public Grade findById(Long id) {
        return this.gradeRepository.findById(id).orElse(null);
    }
    
    

    
}
