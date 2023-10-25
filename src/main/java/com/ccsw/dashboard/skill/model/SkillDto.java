package com.ccsw.dashboard.skill.model;


public class SkillDto {

    private Long id;
    private String name;
    
    private String skillDescription;
    private int skyllLevel;
    

    public String getSkillDescription() {
		return skillDescription;
	}

	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}

	public int getSkyllLevel() {
		return skyllLevel;
	}

	public void setSkyllLevel(int skyllLevel) {
		this.skyllLevel = skyllLevel;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

