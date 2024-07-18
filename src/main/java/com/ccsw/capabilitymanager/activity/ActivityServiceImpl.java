package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void save(ActivityDTO activityDTO) {
            Activity activity = new Activity();
            BeanUtils.copyProperties(activityDTO, activity, "id");
            if (activity.getFechaFinalizacion().before(activity.getFechaInicio())) {
                String respuestaEr = "Error al guardar la actividad. La fecha de finalización tiene que ser mayor o igual a la de inicio";
                throw new UnprocessableEntityException(respuestaEr);
            }

        this.activityRepository.save(activity);
    }

    @Override
    public List<Activity> findAll() {
        return this.activityRepository.findAll();
    }

    @Override
    public List<Activity> findByGgid(String ggid) {
        return activityRepository.findByGgid(ggid).stream().toList();
    }

    public List<Activity> findBySaga(String saga) {
        return activityRepository.findBySaga(saga).stream().toList();
    }

}
