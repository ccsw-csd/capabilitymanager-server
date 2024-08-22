package com.ccsw.capabilitymanager.activitytype;

import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import com.ccsw.capabilitymanager.activitytype.model.ActivityTypeDTO;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity-types")
public class ActivityTypeController {

    @Autowired
    DozerBeanMapper mapper;
    @Autowired
    private ActivityTypeService activityTypeService;

    /**
     * Handles HTTP GET requests to retrieve a list of all activity types.
     *
     * @return A list of {@link ActivityTypeDTO} objects representing all activity types.
     */
    @GetMapping("")
    public List<ActivityTypeDTO> findAll() {
        List<ActivityType> activitiesTypes = activityTypeService.getAllActivityTypes();
        return activitiesTypes.stream().map(activityType -> mapper.map(activityType, ActivityTypeDTO.class)).collect(Collectors.toList());
    }
    
    /**
     * Handles HTTP GET requests to retrieve a specific activity type by its ID.
     *
     * @param id The ID of the activity type to retrieve.
     * @return An {@link Optional} containing the {@link ActivityType} if found, or {@link Optional#empty()} if not.
     */
    @GetMapping("/{id}")
    public Optional<ActivityType> getActivityTypeById(@PathVariable Long id) {
        return activityTypeService.getActivityTypeById(id);
    }
}
