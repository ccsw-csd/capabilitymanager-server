package com.ccsw.capabilitymanager.activitytype;

import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import com.ccsw.capabilitymanager.activitytype.model.ActivityTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Override
    public List<ActivityTypeDTO> getAllActivityTypes() {
        return activityTypeRepository.findAll().stream()
                .map(activityType -> new ActivityTypeDTO(activityType.getId(), activityType.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ActivityType> getActivityTypeById(Long id) {
        return activityTypeRepository.findById(id);
    }
}
