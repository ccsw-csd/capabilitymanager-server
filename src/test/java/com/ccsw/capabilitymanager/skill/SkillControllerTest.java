package com.ccsw.capabilitymanager.skill;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import io.minio.BucketExistsArgs;



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
        nuevo.setName("Java");
        skillList.add(nuevo);

        when(skillService.findAll()).thenReturn(skillList);

        // Act
        List<SkillDto> skillDtoList = skillController.findAll();
        
        assertNotNull(skillDtoList);
        verify(skillService, times(1)).findAll();
	}
	
	@Test
	public void findBySkill() {
		// Arrange
        List<Skill> expectedSkillList = new ArrayList<Skill>();
        String skill = "test";
        
        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setName("Java");
        skill1.setSkillDescription(skill);
        
        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setName("Spring");
        skill2.setSkillDescription(skill);
        
        expectedSkillList.add(skill1);
        expectedSkillList.add(skill2);

        when(skillService.findBySkill(skill)).thenReturn(expectedSkillList);
        
        // Act
        List<Skill> skillList = skillController.findBySkill(skill);        
        
        assertEquals(expectedSkillList, skillList);
        verify(skillService, times(1)).findBySkill(any(String.class));
	}
	
	@Test
	public void findByLocation() {
		// Arrange
        List<Skill> expectedSkillList = new ArrayList<Skill>();
        String location = "test";
        
        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setName("Java");
        skill1.setLocation(location);
        
        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setName("Spring");
        skill2.setLocation(location);
        
        expectedSkillList.add(skill1);
        expectedSkillList.add(skill2);

        when(skillService.findByLocation(location)).thenReturn(expectedSkillList);

        // Act
        List<Skill> skillList = skillController.findByLocation(location);
        
        assertEquals(expectedSkillList, skillList);
        verify(skillService, times(1)).findByLocation(any(String.class));
	}
	

}
