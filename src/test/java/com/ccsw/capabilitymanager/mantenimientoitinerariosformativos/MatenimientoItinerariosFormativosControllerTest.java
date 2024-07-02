package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

public class MatenimientoItinerariosFormativosControllerTest {


    private MockMvc mockMvc;

    @Mock
    private MantenimientoItinerariosFormativosService mantenimientoIntinerariosFormativosService;

    @InjectMocks
    private MatenimientoItinerariosFormativosController mantenimientoItinerariosFormativosController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mantenimientoItinerariosFormativosController).build();
    }

    @Test
    void testGetAll() throws Exception {
        ItinerariosFormativos itinerario1 = new ItinerariosFormativos();
        itinerario1.setId(1L);
        ItinerariosFormativos itinerario2 = new ItinerariosFormativos();
        itinerario2.setId(2L);

        when(mantenimientoIntinerariosFormativosService.findAll()).thenReturn(Arrays.asList(itinerario1, itinerario2));

        mockMvc.perform(get("/mantenimiento/itinerariosFormativos/showAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(mantenimientoIntinerariosFormativosService, times(1)).findAll();
    }

    @Test
    void testSetItinerarioFormativo() throws Exception {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setId(1L);
        // Set other properties of dto if necessary

        mockMvc.perform(post("/mantenimiento/itinerariosFormativos/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1 }")) // Add other properties to the JSON if necessary
                .andExpect(status().isOk());

        verify(mantenimientoIntinerariosFormativosService, times(1)).save(any(ItinerariosFormativosDto.class));
    }

    @Test
    void testUpdateItinerarioFormativo() throws Exception {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setId(1L);
        // Set other properties of dto if necessary

        mockMvc.perform(put("/mantenimiento/itinerariosFormativos/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1 }")) // Add other properties to the JSON if necessary
                .andExpect(status().isOk());

        verify(mantenimientoIntinerariosFormativosService, times(1)).update(any(ItinerariosFormativosDto.class));
    }

    @Test
    void testEliminarItinerarioFormativo() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/mantenimiento/itinerariosFormativos/delete/{id}", id))
                .andExpect(status().isOk());

        verify(mantenimientoIntinerariosFormativosService, times(1)).delete(id);
    }
}
