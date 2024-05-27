package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;

import java.util.List;

public interface ActivityService {

    List<Activity> findAll();

    List<Activity> findBySaga(String saga);
}
