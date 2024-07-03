package com.ccsw.capabilitymanager.skill;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import com.ccsw.capabilitymanager.skill.model.Skill;
import com.ccsw.capabilitymanager.skill.model.SkillDto;



public class SkillControllerTest {
	
	
	@InjectMocks
	private SkillController skillController;
	
	@Mock
	private SkillService skillService;
	
	@Mock
	private DozerBeanMapper mapper;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void findAll() {
		// Arrange
        List<Skill> skillList = new ArrayList<Skill>();
        Skill nuevo = new Skill();
        nuevo.setId(1L);
        skillList.add(nuevo);

        when(skillService.findAll()).thenReturn(skillList);

        // Act
        List<SkillDto> skillDtoList = skillController.findAll();
        
        assertNotNull(skillDtoList);
	}
	

	

}
