package com.ccsw.capabilitymanager.activitytype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ccsw.capabilitymanager.activitytype.ActivityTypeController;
import com.ccsw.capabilitymanager.activitytype.ActivityTypeService;
import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import com.ccsw.capabilitymanager.activitytype.model.ActivityTypeDTO;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActivityTypeControllerTest {
    @InjectMocks
    private ActivityTypeController activityTypeController;

    @Mock
    private ActivityTypeService activityTypeService;

    @Mock
    private DozerBeanMapper mapper;

    private ActivityType activityType1;
    private ActivityType activityType2;
    private ActivityTypeDTO activityTypeDTO1;
    private ActivityTypeDTO activityTypeDTO2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activityType1 = new ActivityType();
        activityType1.setId(1L);

        activityType2 = new ActivityType();
        activityType2.setId(2L);

        activityTypeDTO1 = new ActivityTypeDTO();
        activityTypeDTO1.setId(1L);

        activityTypeDTO2 = new ActivityTypeDTO();
        activityTypeDTO2.setId(2L);
    }

    @Test
    public void testFindAll() {
        List<ActivityType> activityTypes = Arrays.asList(activityType1, activityType2);

        when(activityTypeService.getAllActivityTypes()).thenReturn(activityTypes);
        when(mapper.map(activityType1, ActivityTypeDTO.class)).thenReturn(activityTypeDTO1);
        when(mapper.map(activityType2, ActivityTypeDTO.class)).thenReturn(activityTypeDTO2);

        List<ActivityTypeDTO> result = activityTypeController.findAll();

        assertEquals(2, result.size());
        assertEquals(activityTypeDTO1, result.get(0));
        assertEquals(activityTypeDTO2, result.get(1));
    }

    @Test
    public void testGetActivityTypeById() {
        Long id = 1L;
        Optional<ActivityType> optionalActivityType = Optional.of(activityType1);

        when(activityTypeService.getActivityTypeById(id)).thenReturn(optionalActivityType);

        Optional<ActivityType> result = activityTypeController.getActivityTypeById(id);

        assertEquals(optionalActivityType, result);
    }
}
