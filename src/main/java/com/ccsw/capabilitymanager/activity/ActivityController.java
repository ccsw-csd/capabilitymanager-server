package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    DozerBeanMapper mapper;
    @Autowired
    private ActivityService activityService;

    @GetMapping("")
    public List<ActivityDTO> findAll() {
        List<Activity> activities = activityService.findAll();
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/{saga}")
    public List<ActivityDTO> findBySaga(@PathVariable String saga) {
        List<Activity> activities = activityService.findBySaga(saga);
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }
}
