package com.ccsw.capabilitymanager.listadobench.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;
import com.ccsw.capabilitymanager.listadobench.controller.ListadoBenchController;

public class ListadoBenchControllerTest {
    @Mock
    private ListadoBenchService listadoBenchService;

    @InjectMocks
    private ListadoBenchController listadoBenchController;

    public ListadoBenchControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStaffing_shouldReturnListOfListadoBench() {
        ListadoBench entry1 = new ListadoBench();
        ListadoBench entry2 = new ListadoBench();
        List<ListadoBench> expectedList = Arrays.asList(entry1, entry2);

        when(listadoBenchService.getListadoPersonasBench()).thenReturn(expectedList);

        List<ListadoBench> result = listadoBenchController.getAllStaffing();

        assertEquals(expectedList, result);
    }

    @Test
    void getEmpleadoPorSaga_shouldReturnOptionalListOfListadoBench() {
        String saga = "saga1";
        ListadoBench entry1 = new ListadoBench();
        List<ListadoBench> expectedList = Arrays.asList(entry1);
        Optional<List<ListadoBench>> expectedOptional = Optional.of(expectedList);

        when(listadoBenchService.getEmpleadoPorSaga(saga)).thenReturn(expectedOptional);

        Optional<List<ListadoBench>> result = listadoBenchController.getEmpleadoPorSaga(saga);

        assertEquals(expectedOptional, result);
    }

    @Test
    void getEmpleadoPorSaga_shouldReturnEmptyOptionalWhenNoEntriesFound() {
        String saga = "nonexistentSaga";
        Optional<List<ListadoBench>> expectedOptional = Optional.empty();

        when(listadoBenchService.getEmpleadoPorSaga(saga)).thenReturn(expectedOptional);

        Optional<List<ListadoBench>> result = listadoBenchController.getEmpleadoPorSaga(saga);

        assertEquals(expectedOptional, result);
    }
}
