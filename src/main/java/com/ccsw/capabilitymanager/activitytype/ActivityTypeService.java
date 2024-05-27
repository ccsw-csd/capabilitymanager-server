package com.ccsw.capabilitymanager.activitytype;

import com.ccsw.capabilitymanager.activitytype.model.ActivityType;

import java.util.List;
import java.util.Optional;

public interface ActivityTypeService {

    List<ActivityType> getAllActivityTypes();

    Optional<ActivityType> getActivityTypeById(Long id);
}
