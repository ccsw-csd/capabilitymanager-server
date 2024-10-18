package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;

import java.text.ParseException;
import java.util.List;

public interface ActivityService {

    List<Activity> findAll();

    List<Activity> findByGgid(String ggid);

    List<Activity> findBySaga(String saga);

    void save(ActivityDTO activityDto);

	void delete(Long id);

    void update(ActivityDTO activityDto) throws ParseException;
}
