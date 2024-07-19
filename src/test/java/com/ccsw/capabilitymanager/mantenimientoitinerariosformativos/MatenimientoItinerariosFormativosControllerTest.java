package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatenimientoItinerariosFormativosControllerTest {
	
	    @Autowired
	    private MockMvc mockMvc;

	    @Mock
	    private MantenimientoItinerariosFormativosService mantenimientoIntinerariosFormativosService;

	    @InjectMocks
	    private MatenimientoItinerariosFormativosController mantenimientoIntinerariosFormativosController;



	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(mantenimientoIntinerariosFormativosController).build();
	    }

	    @Test
	    void testGetAll() throws Exception {
	        List<ItinerariosFormativos> itinerarios = new ArrayList<>();
	        ItinerariosFormativos itinerariosFormativos = new ItinerariosFormativos();
	        itinerariosFormativos.setId(1L);
	        itinerariosFormativos.setCodigo("IT001");
	        itinerariosFormativos.setName("Itinerario 1");
	        itinerariosFormativos.setUsuario("usuario1");
	        itinerariosFormativos.setFecha_Alta(new Date());
	        itinerariosFormativos.setFecha_Baja(new Date());
	        itinerariosFormativos.setFecha_Modif(new Date());
	        itinerarios.add(itinerariosFormativos);
	        when(mantenimientoIntinerariosFormativosService.findAll()).thenReturn(itinerarios);

	        mockMvc.perform(get("/mantenimiento/itinerariosFormativos/showAll"))
	                .andExpect(status().isOk());

	        verify(mantenimientoIntinerariosFormativosService, times(1)).findAll();
	    }

	    @Test
	    void testSetItinerarioFormativo() throws Exception {
	        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
	        dto.setCodigo("IT001");
	        dto.setName("Itinerario 1");
	        dto.setUsuario("usuario1");
	        ObjectMapper objectMapper = new ObjectMapper();
	        String dtoJson = objectMapper.writeValueAsString(dto);

	        mockMvc.perform(post("/mantenimiento/itinerariosFormativos/insert")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(dtoJson))
	                .andExpect(status().isOk());

	        verify(mantenimientoIntinerariosFormativosService, times(1)).save(any(ItinerariosFormativosDto.class));
	    }

	    @Test
	    void testUpdateItinerarioFormativo() throws Exception {
	        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
	        dto.setCodigo("IT001");
	        dto.setName("Itinerario 1");
	        dto.setUsuario("usuario1");
	        ObjectMapper objectMapper = new ObjectMapper();
	        String dtoJson = objectMapper.writeValueAsString(dto);

	        mockMvc.perform(put("/mantenimiento/itinerariosFormativos/update")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(dtoJson))
	                .andExpect(status().isOk());

	        verify(mantenimientoIntinerariosFormativosService, times(1)).update(any(ItinerariosFormativosDto.class));
	    }

	    @Test
	    void testDeleteItinerarioFormativo() throws Exception {
	        Long id = 1L;

	        mockMvc.perform(delete("/mantenimiento/itinerariosFormativos/delete/{id}", id))
	                .andExpect(status().isOk());

	        verify(mantenimientoIntinerariosFormativosService, times(1)).delete(id);
	    }
	}