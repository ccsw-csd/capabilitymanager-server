package com.ccsw.capabilitymanager.config.grade;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.config.grade.model.Grade;

import java.util.List;

@Service
@Transactional
public class GradeServiceImpl implements GradeService{

    @Autowired
    private GradeRepository gradeRepository;
    
    /**
     * Retrieves a list of all grades, sorted in their natural order.
     *
     * @return A list of {@link Grade} objects representing all grades, sorted by their natural ordering.
     */  
    @Override
    public List<Grade> findAll() {
        return (List<Grade>) this.gradeRepository.findAll().stream().sorted().toList();
    }
    
    /**
     * Retrieves a grade by its name or identifier.
     *
     * @param grade The name or identifier of the grade to retrieve.
     * @return The {@link Grade} object associated with the specified name or identifier, or {@code null} if no such grade exists.
     */
    @Override
    public Grade findByGrade(String grade) {
        return this.gradeRepository.findByGrade(grade);
    }
    
    /**
     * Retrieves a grade by its ID.
     *
     * @param id The ID of the grade to retrieve.
     * @return The {@link Grade} object with the specified ID, or {@code null} if no grade with the given ID exists.
     */    
    @Override
    public Grade findById(Long id) {
        return this.gradeRepository.findById(id).orElse(null);
    }
    
    

    
}
