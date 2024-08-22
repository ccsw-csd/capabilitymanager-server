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

    /**
     * Retrieves a list of all activity types.
     *
     * @return A list of {@link ActivityType} objects representing all activity types in the repository.
     */
    @Override
    public List<ActivityType> getAllActivityTypes() {
        return this.activityTypeRepository.findAll();
    }
    
    /**
     * Retrieves an activity type by its ID.
     *
     * @param id The ID of the activity type to retrieve.
     * @return An {@link Optional} containing the {@link ActivityType} if found, or {@link Optional#empty()} if not.
     */
    @Override
    public Optional<ActivityType> getActivityTypeById(Long id) {
        return activityTypeRepository.findById(id);
    }
}
