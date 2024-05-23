package com.ccsw.capabilitymanager.activity;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.activity.model.Activity;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    DozerBeanMapper mapper;

    @GetMapping("/all")
    public List<Activity> findAll() {
        return this.activityService.findAll();
    }

}
