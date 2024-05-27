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

    @GetMapping("")
    public List<ActivityTypeDTO> findAll() {
        List<ActivityType> activitiesTypes = activityTypeService.getAllActivityTypes();
        return activitiesTypes.stream().map(activityType -> mapper.map(activityType, ActivityTypeDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<ActivityType> getActivityTypeById(@PathVariable Long id) {
        return activityTypeService.getActivityTypeById(id);
    }
}
