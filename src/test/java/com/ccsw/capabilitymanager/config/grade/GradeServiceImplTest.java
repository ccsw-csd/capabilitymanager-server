package com.ccsw.capabilitymanager.config.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ccsw.capabilitymanager.config.grade.model.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GradeServiceImplTest {
    @InjectMocks
    private GradeServiceImpl gradeService;

    @Mock
    private GradeRepository gradeRepository;

    private Grade grade1;
    private Grade grade2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        grade1 = new Grade();
        grade1.setId(1L);
        grade1.setGrade("Grade A");

        grade2 = new Grade();
        grade2.setId(2L);
        grade2.setGrade("Grade B");
    }

    @Test
    public void testFindAll() {
        List<Grade> grades = Arrays.asList(grade1, grade2);

        when(gradeRepository.findAll()).thenReturn(grades);

        List<Grade> result = gradeService.findAll();

        assertEquals(2, result.size());
        assertEquals(grade1, result.get(0));
        assertEquals(grade2, result.get(1));
    }

    @Test
    public void testFindByGrade() {
        String gradeName = "Grade A";

        when(gradeRepository.findByGrade(gradeName)).thenReturn(grade1);

        Grade result = gradeService.findByGrade(gradeName);

        assertEquals(grade1, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        when(gradeRepository.findById(id)).thenReturn(Optional.of(grade1));

        Grade result = gradeService.findById(id);

        assertEquals(grade1, result);
    }

}
