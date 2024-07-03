package com.ccsw.capabilitymanager.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ActivityServiceImplTest {
    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityRepository activityRepository;

    private Activity activity1;
    private Activity activity2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activity1 = new Activity();
        activity1.setId(1L);
        activity1.setNombreActividad("Activity 1");

        activity2 = new Activity();
        activity2.setId(2L);
        activity2.setNombreActividad("Activity 2");
    }

    @Test
    public void testFindAll() {
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityRepository.findAll()).thenReturn(activities);

        List<Activity> result = activityService.findAll();

        assertEquals(2, result.size());
        assertEquals(activity1, result.get(0));
        assertEquals(activity2, result.get(1));
    }

    @Test
    public void testFindByGgid() {
        String ggid = "testGgid";
        List<Activity> activities = Arrays.asList(activity1);

        when(activityRepository.findByGgid(ggid)).thenReturn(activities);

        List<Activity> result = activityService.findByGgid(ggid);

        assertEquals(1, result.size());
        assertEquals(activity1, result.get(0));
    }

    @Test
    public void testFindBySaga() {
        String saga = "testSaga";
        List<Activity> activities = Arrays.asList(activity2);

        when(activityRepository.findBySaga(saga)).thenReturn(activities);

        List<Activity> result = activityService.findBySaga(saga);

        assertEquals(1, result.size());
        assertEquals(activity2, result.get(0));
    }
}
