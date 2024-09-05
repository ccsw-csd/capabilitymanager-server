package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

public class MantenimientoItinerariosFormativosServiceImplTest {
	
    @Mock
    private MantenimientoItinerariosFormativosRepository mantenimientoItinerariosFormativosRepository;

    @InjectMocks
    private MantenimientoItinerariosFormativosServiceImpl mantenimientoItinerariosFormativosService;


    private ItinerariosFormativos itinerariosFormativos;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() throws ParseException {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setCodigo("IT011");
        dto.setName("Itinerario 1");
        dto.setUsuario("usuario1");
        
        ItinerariosFormativos itinerariosFormativos = new ItinerariosFormativos();
        itinerariosFormativos.setId(1L);
        itinerariosFormativos.setCodigo("IT001");
        itinerariosFormativos.setName("Itinerario 1");
        itinerariosFormativos.setUsuario("usuario1");
        itinerariosFormativos.setFecha_Alta(new Date());
        itinerariosFormativos.setFecha_Baja(new Date());
        itinerariosFormativos.setFecha_Modif(new Date());
        
        when(mantenimientoItinerariosFormativosRepository.findByCodigo(anyString())).thenReturn(null);
        
        mantenimientoItinerariosFormativosService.save(dto);
        
        verify(mantenimientoItinerariosFormativosRepository).save(any(ItinerariosFormativos.class));
    }

    @Test
    void testSaveItinerarioExistente() {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setCodigo("IT001");
        dto.setName("Itinerario 1");
        dto.setUsuario("usuario1");
        
        ItinerariosFormativos itinerariosFormativos = new ItinerariosFormativos();
        itinerariosFormativos.setId(1L);
        itinerariosFormativos.setCodigo("IT001");
        itinerariosFormativos.setName("Itinerario 1");
        itinerariosFormativos.setUsuario("usuario1");
        itinerariosFormativos.setFecha_Alta(new Date());
        itinerariosFormativos.setFecha_Baja(new Date());
        itinerariosFormativos.setFecha_Modif(new Date());
        
        when(mantenimientoItinerariosFormativosRepository.findByCodigo(anyString())).thenReturn(itinerariosFormativos);

        assertThrows(UnprocessableEntityException.class, () -> {
            mantenimientoItinerariosFormativosService.save(dto);
        });
    }

    @Test
    void testUpdate() throws ParseException {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setCodigo("IT001");
        dto.setName("Itinerario 1");
        dto.setUsuario("usuario1");
        
        ItinerariosFormativos itinerariosFormativos = new ItinerariosFormativos();
        itinerariosFormativos.setId(1L);
        itinerariosFormativos.setCodigo("IT001");
        itinerariosFormativos.setName("Itinerario 1");
        itinerariosFormativos.setUsuario("usuario1");
        itinerariosFormativos.setFecha_Alta(new Date());
        itinerariosFormativos.setFecha_Baja(new Date());
        itinerariosFormativos.setFecha_Modif(new Date());
        
        when(mantenimientoItinerariosFormativosRepository.findByid(anyLong())).thenReturn(itinerariosFormativos);
        dto.setId(itinerariosFormativos.getId());
        mantenimientoItinerariosFormativosService.update(dto);
        
        verify(mantenimientoItinerariosFormativosRepository).save(any(ItinerariosFormativos.class));
        assertEquals(dto.getName(), itinerariosFormativos.getName());
    }

    
    @Test
    void testFindAll() {
        List<ItinerariosFormativos> itinerarios = new ArrayList<>();
        
        itinerarios.add(itinerariosFormativos);
        when(mantenimientoItinerariosFormativosRepository.findAll()).thenReturn(itinerarios);

        List<ItinerariosFormativos> result = mantenimientoItinerariosFormativosService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        ItinerariosFormativos itinerariosFormativos = new ItinerariosFormativos();
        itinerariosFormativos.setId(1L);
        itinerariosFormativos.setCodigo("IT001");
        itinerariosFormativos.setName("Itinerario 1");
        itinerariosFormativos.setUsuario("usuario1");
        itinerariosFormativos.setFecha_Alta(new Date());
        itinerariosFormativos.setFecha_Baja(new Date());
        itinerariosFormativos.setFecha_Modif(new Date());
        when(mantenimientoItinerariosFormativosRepository.findByid(itinerariosFormativos.getId())).thenReturn(itinerariosFormativos);

        mantenimientoItinerariosFormativosService.delete(itinerariosFormativos.getId());

        verify(mantenimientoItinerariosFormativosRepository).delete(itinerariosFormativos);
    }
}