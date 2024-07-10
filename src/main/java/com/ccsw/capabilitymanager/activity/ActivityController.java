package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.dataimport.DataImportService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    DozerBeanMapper mapper;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private DataImportService dataImportService;


    @GetMapping("")
    public List<ActivityDTO> findAll() {
        List<Activity> activities = activityService.findAll();
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/ggid/{ggid}")
    public List<ActivityDTO> findByGgid(@PathVariable String ggid) {
        List<Activity> activities = activityService.findByGgid(ggid);
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/saga/{saga}")
    public List<ActivityDTO> findBySaga(@PathVariable String saga) {
        List<Activity> activities = activityService.findBySaga(saga);
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }

    @PostMapping("/guardar")
    public void guardarActividad(@RequestBody ActivityDTO activityDto) {
        this.dataImportService.saveActividad(activityDto);
    }
}
