package com.ccsw.capabilitymanager.activitytype;

import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Override
    public List<ActivityType> getAllActivityTypes() {
        return this.activityTypeRepository.findAll();
    }

    @Override
    public Optional<ActivityType> getActivityTypeById(Long id) {
        return activityTypeRepository.findById(id);
    }
}
