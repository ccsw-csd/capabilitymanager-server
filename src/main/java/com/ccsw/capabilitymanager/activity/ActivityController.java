package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImport;
import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImportDto;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.dataimport.DataImportService;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;

import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    DozerBeanMapper mapper;
    @Autowired
    private ActivityService activityService;

    /**
     * Handles HTTP GET requests to retrieve a list of all activities.
     *
     * @return A list of ActivityDTO objects representing all activities.
     */
    @GetMapping("")
    public List<ActivityDTO> findAll() {
        List<Activity> activities = activityService.findAll();
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }

    /**
     * Handles HTTP GET requests to retrieve a list of activities filtered by a specific GGID.
     *
     * @param ggid The GGID used to filter the activities.
     * @return A list of {@link ActivityDTO} objects representing the activities associated with the given GGID.
     */
    @GetMapping("/ggid/{ggid}")
    public List<ActivityDTO> findByGgid(@PathVariable String ggid) {
        List<Activity> activities = activityService.findByGgid(ggid);
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }
    
    /**
     * Handles HTTP GET requests to retrieve a list of activities filtered by a specific saga.
     *
     * @param saga The saga used to filter the activities.
     * @return A list of {@link ActivityDTO} objects representing the activities associated with the given saga.
     */
    @GetMapping("/saga/{saga}")
    public List<ActivityDTO> findBySaga(@PathVariable String saga) {
        List<Activity> activities = activityService.findBySaga(saga);
        return activities.stream().map(activity -> mapper.map(activity, ActivityDTO.class)).collect(Collectors.toList());
    }
    
    /**
     * Handles HTTP POST requests to save a new activity.
     *
     * @param activityDto The {@link ActivityDTO} object containing the details of the activity to be saved.
     */
    @PostMapping("/guardar")
    public void guardarActividad(@RequestBody ActivityDTO activityDto) {
        this.activityService.save(activityDto);
    }
    
    /**
     * Deletes an {@link Activity} entity by its ID.
     *
     * <p>This method receives an ID as a path variable, logs a debug message, and then deletes the corresponding
     * {@link Activity} entity using the service layer.</p>
     *
     * @param id The ID of the {@link Activity} entity to be deleted.
     * @throws ParseException If there is an error related to parsing during the deletion process.
     */
    @DeleteMapping(path = "delete/{id}")
    public void eliminarActividad(@PathVariable(name = "id", required = false) Long id) throws ParseException{
        CapabilityLogger.logDebug("Delete activity with id:" + id);
    
    	this.activityService.delete(id);
    }

    /**
     * Updates an {@link Activity} entity based on the provided {@link ActivityDTO}.
     *
     * <p>This method receives an {@link ActivityDTO} object as a request body, logs a debug message,
     * and updates the corresponding {@link Activity} entity using the service layer.</p>
     *
     * @param activityDto The {@link ActivityDTO} object containing the updated information for the activity.
     * @throws ParseException If there is an error related to parsing during the update process.
     */
    @PutMapping(path = "/update")
    public void updateActividad(@RequestBody ActivityDTO activityDto) throws ParseException{
        CapabilityLogger.logDebug("Update activity");

        activityService.update(activityDto);
    }
}
