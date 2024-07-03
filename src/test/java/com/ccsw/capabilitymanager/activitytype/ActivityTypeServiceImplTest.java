package com.ccsw.capabilitymanager.activitytype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ccsw.capabilitymanager.activitytype.ActivityTypeService;
import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import com.ccsw.capabilitymanager.activitytype.ActivityTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActivityTypeServiceImplTest {
    @InjectMocks
    private ActivityTypeServiceImpl activityTypeService;

    @Mock
    private ActivityTypeRepository activityTypeRepository;

    private ActivityType activityType1;
    private ActivityType activityType2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activityType1 = new ActivityType();
        activityType1.setId(1L);

        activityType2 = new ActivityType();
        activityType2.setId(2L);
    }

    @Test
    public void testGetAllActivityTypes() {
        List<ActivityType> activityTypes = Arrays.asList(activityType1, activityType2);

        when(activityTypeRepository.findAll()).thenReturn(activityTypes);

        List<ActivityType> result = activityTypeService.getAllActivityTypes();

        assertEquals(2, result.size());
        assertEquals(activityType1, result.get(0));
        assertEquals(activityType2, result.get(1));
    }

    @Test
    public void testGetActivityTypeById() {
        Long id = 1L;
        Optional<ActivityType> optionalActivityType = Optional.of(activityType1);

        when(activityTypeRepository.findById(id)).thenReturn(optionalActivityType);

        Optional<ActivityType> result = activityTypeService.getActivityTypeById(id);

        assertEquals(optionalActivityType, result);
    }
}
