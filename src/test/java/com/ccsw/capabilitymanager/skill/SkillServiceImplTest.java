package com.ccsw.capabilitymanager.skill;

import static org.mockito.Mockito.when;
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
		List<Skill> list = new ArrayList<Skill>();
		
		when(skillRepository.findAll()).thenReturn(list);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findAll();
		
		assertNotNull(skillList);
	}
	
	@Test
	public void findBySkill() {
		// Arrange
		List<Skill> list = new ArrayList<Skill>();
		String skill = "";
		
		when(skillRepository.findBySkillDescription(skill)).thenReturn(list);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findBySkill(skill);
		
		assertNotNull(skillList);
	}
	
	@Test
	public void findByLocation() {
		// Arrange
		List<Skill> list = new ArrayList<Skill>();
		String location = "";
		
		when(skillRepository.findSkillByLocation(location)).thenReturn(list);
		
		//Act
		List<Skill> skillList = skillServiceImpl.findByLocation(location);
		
		assertNotNull(skillList);
	}
	
	
	

}
