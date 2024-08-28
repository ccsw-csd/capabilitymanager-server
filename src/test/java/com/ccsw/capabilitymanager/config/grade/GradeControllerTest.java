package com.ccsw.capabilitymanager.config.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.config.grade.model.Grade;
import com.ccsw.capabilitymanager.config.grade.model.GradeDto;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class GradeControllerTest {

        @InjectMocks
        private GradeController gradeController;

        @Mock
        private GradeService gradeService;

        @Mock
        private DozerBeanMapper mapper;

        private Grade grade1;
        private Grade grade2;
        private GradeDto gradeDto1;
        private GradeDto gradeDto2;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            grade1 = new Grade();
            grade1.setId(1L);

            grade2 = new Grade();
            grade2.setId(2L);

            gradeDto1 = new GradeDto();
            gradeDto1.setId(1);

            gradeDto2 = new GradeDto();
            gradeDto2.setId(2);
        }

        @Test
        public void testFindAll() {
            List<Grade> grades = Arrays.asList(grade1, grade2);

            when(gradeService.findAll()).thenReturn(grades);
            when(mapper.map(grade1, GradeDto.class)).thenReturn(gradeDto1);
            when(mapper.map(grade2, GradeDto.class)).thenReturn(gradeDto2);

            List<GradeDto> result = gradeController.findAll();

            assertEquals(2, result.size());
            assertEquals(gradeDto1, result.get(0));
            assertEquals(gradeDto2, result.get(1));
        }

        @Test
        public void testFindById() {
            Long id = 1L;

            when(gradeService.findById(id)).thenReturn(grade1);

            Grade result = gradeController.findById(id.toString());

            assertEquals(grade1, result);
        }
}
