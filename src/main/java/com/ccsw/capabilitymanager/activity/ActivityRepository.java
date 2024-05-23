package com.ccsw.capabilitymanager.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccsw.capabilitymanager.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
    
}
