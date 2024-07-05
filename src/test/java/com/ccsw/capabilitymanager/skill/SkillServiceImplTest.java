package com.ccsw.capabilitymanager.skill;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.skill.model.Skill;

public class SkillServiceImplTest {
	
	@InjectMocks
	private SkillServiceImpl skillServiceImpl;
	
	@Mock
	private SkillRepository skillRepository;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void findAll() {
		// Arrange
		List<Skill> expectedSkillList = new ArrayList<Skill>();
        Skill nuevo = new Skill();
        nuevo.setId(1L);
        nuevo.setName("Java");
        expectedSkillList.add(nuevo);
		
		when(skillRepository.findAll()).thenReturn(expectedSkillList);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findAll();
		
		assertEquals(expectedSkillList, skillList);
        verify(skillRepository, times(1)).findAll();
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

		
		when(skillRepository.findBySkillDescription(skill)).thenReturn(expectedSkillList);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findBySkill(skill);
		
		assertEquals(expectedSkillList, skillList);
        verify(skillRepository, times(1)).findBySkillDescription(any(String.class));
		
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
		
		when(skillRepository.findSkillByLocation(location)).thenReturn(expectedSkillList);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findByLocation(location);
		
		assertEquals(expectedSkillList, skillList);
        verify(skillRepository, times(1)).findSkillByLocation(any(String.class));
	}
	
	
	

}
