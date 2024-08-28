package com.ccsw.capabilitymanager.listadobench.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.repository.ViewListadoBenchRepository;
import com.ccsw.capabilitymanager.listadobench.service.impl.ListadoBenchServiceImpl;

public class ListadoBenchServiceImplTest {
    @Mock
    private ViewListadoBenchRepository listadoBenchRepository;

    @InjectMocks
    private ListadoBenchServiceImpl listadoBenchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmpleadoPorSaga_shouldReturnOptionalListOfListadoBench() {
        String saga = "saga1";
        ListadoBench entry1 = new ListadoBench();
        List<ListadoBench> expectedList = Arrays.asList(entry1);
        Optional<List<ListadoBench>> expectedOptional = Optional.of(expectedList);

        when(listadoBenchRepository.getEmpleadoPorSaga(saga)).thenReturn(expectedOptional);

        Optional<List<ListadoBench>> result = listadoBenchService.getEmpleadoPorSaga(saga);

        assertEquals(expectedOptional, result);
    }

    @Test
    void getEmpleadoPorSaga_shouldReturnEmptyOptionalWhenNoEntriesFound() {
        String saga = "nonexistentSaga";
        Optional<List<ListadoBench>> expectedOptional = Optional.empty();

        when(listadoBenchRepository.getEmpleadoPorSaga(saga)).thenReturn(expectedOptional);

        Optional<List<ListadoBench>> result = listadoBenchService.getEmpleadoPorSaga(saga);

        assertEquals(expectedOptional, result);
    }

    @Test
    void getListadoPersonasBench_shouldReturnListOfListadoBench() {
        ListadoBench entry1 = new ListadoBench();
        ListadoBench entry2 = new ListadoBench();
        List<ListadoBench> expectedList = Arrays.asList(entry1, entry2);

        when(listadoBenchRepository.getListadoPersonasBench()).thenReturn(expectedList);

        List<ListadoBench> result = listadoBenchService.getListadoPersonasBench();

        assertEquals(expectedList, result);
    }
}
