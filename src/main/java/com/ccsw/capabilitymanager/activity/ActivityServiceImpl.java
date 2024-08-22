package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
	private static final String ERROR_INIT = ">>> [ERROR][ActivityServiceImpl] (";
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Saves an activity after validating the data.
     *
     * @param activityDTO The ActivityDTO object containing the activity details to be saved.
     * @throws UnprocessableEntityException if the end date is before the start date.
     */
    @Override
    public void save(ActivityDTO activityDTO) {
            Activity activity = new Activity();
            BeanUtils.copyProperties(activityDTO, activity, "id");
            if (activity.getFechaFinalizacion().before(activity.getFechaInicio())) {
                String respuestaEr = ERROR_INIT +"save): Error al guardar la actividad. La fecha de finalización tiene que ser mayor o igual a la de inicio";
                CapabilityLogger.logError(respuestaEr);
                throw new UnprocessableEntityException(respuestaEr);
            }

        this.activityRepository.save(activity);
    }
    
    /**
     * Retrieves a list of all activities.
     *
     * @return A list of {@link Activity} objects representing all activities in the repository.
     */
    @Override
    public List<Activity> findAll() {
        return this.activityRepository.findAll();
    }

    /**
     * Retrieves a list of activities filtered by a specific GGID.
     *
     * @param ggid The GGID used to filter the activities.
     * @return A list of {@link Activity} objects associated with the given GGID.
     */
    @Override
    public List<Activity> findByGgid(String ggid) {
        return activityRepository.findByGgid(ggid).stream().toList();
    }

    /**
     * Retrieves a list of activities filtered by a specific saga.
     *
     * @param saga The saga used to filter the activities.
     * @return A list of {@link Activity} objects associated with the given saga.
     */
    public List<Activity> findBySaga(String saga) {
        return activityRepository.findBySaga(saga).stream().toList();
    }

}
