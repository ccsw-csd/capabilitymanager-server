package com.ccsw.capabilitymanager.activity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.activity.model.Activity;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService{
    
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<Activity> findAll() {
    	return (List<Activity>) this.activityRepository.findAll();
    }
}
