package com.ccsw.capabilitymanager.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActivityControllerTest {
    @InjectMocks
    private ActivityController activityController;

    @Mock
    private ActivityService activityService;

    @Mock
    private DozerBeanMapper mapper;

    private Activity activity1;
    private Activity activity2;
    private ActivityDTO activityDTO1;
    private ActivityDTO activityDTO2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activity1 = new Activity();
        activity1.setId(1L);
        activity1.setNombreActividad("Activity 1");

        activity2 = new Activity();
        activity2.setId(2L);
        activity2.setNombreActividad("Activity 2");

        activityDTO1 = new ActivityDTO();
        activityDTO1.setId(1L);
        activityDTO1.setNombreActividad("Activity 1");

        activityDTO2 = new ActivityDTO();
        activityDTO2.setId(2L);
        activityDTO2.setNombreActividad("Activity 2");
    }

    @Test
    public void testFindAll() {
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityService.findAll()).thenReturn(activities);
        when(mapper.map(activity1, ActivityDTO.class)).thenReturn(activityDTO1);
        when(mapper.map(activity2, ActivityDTO.class)).thenReturn(activityDTO2);

        List<ActivityDTO> result = activityController.findAll();

        assertEquals(2, result.size());
        assertEquals(activityDTO1, result.get(0));
        assertEquals(activityDTO2, result.get(1));
    }

    @Test
    public void testFindByGgid() {
        String ggid = "testGgid";
        List<Activity> activities = Arrays.asList(activity1);

        when(activityService.findByGgid(ggid)).thenReturn(activities);
        when(mapper.map(activity1, ActivityDTO.class)).thenReturn(activityDTO1);

        List<ActivityDTO> result = activityController.findByGgid(ggid);

        assertEquals(1, result.size());
        assertEquals(activityDTO1, result.get(0));
    }

    @Test
    public void testFindBySaga() {
        String saga = "testSaga";
        List<Activity> activities = Arrays.asList(activity2);

        when(activityService.findBySaga(saga)).thenReturn(activities);
        when(mapper.map(activity2, ActivityDTO.class)).thenReturn(activityDTO2);

        List<ActivityDTO> result = activityController.findBySaga(saga);

        assertEquals(1, result.size());
        assertEquals(activityDTO2, result.get(0));
    }
}
