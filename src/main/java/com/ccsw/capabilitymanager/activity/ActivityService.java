package com.ccsw.capabilitymanager.activity;

import java.util.List;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;

public interface ActivityService {

    List<Activity> findAll();

    List<Activity> findBySaga(String saga);
}
