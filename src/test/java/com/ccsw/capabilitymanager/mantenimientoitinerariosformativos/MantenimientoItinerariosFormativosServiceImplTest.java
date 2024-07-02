package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ccsw.capabilitymanager.exception.ItinerarioExistenteException;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

public class MantenimientoItinerariosFormativosServiceImplTest {


    @Mock
    private MantenimientoItinerariosFormativosRepository mantenimientoItinerariosFormativosRepository;

    @InjectMocks
    private MantenimientoItinerariosFormativosServiceImpl mantenimientoItinerariosFormativosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() throws ParseException {
        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setCodigo("testCodigo");
        dto.setName("testName");
        dto.setUsuario("testUsuario");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaEspecifica = formatter.parse("9999-12-31");

        mantenimientoItinerariosFormativosService.save(dto);

        verify(mantenimientoItinerariosFormativosRepository, times(1)).save(any(ItinerariosFormativos.class));
    }

    @Test
    void testUpdate() throws ParseException {
        ItinerariosFormativos existingItinerario = new ItinerariosFormativos();
        existingItinerario.setId(1L);
        existingItinerario.setCodigo("oldCodigo");

        when(mantenimientoItinerariosFormativosRepository.findByid(1L)).thenReturn(existingItinerario);

        ItinerariosFormativosDto dto = new ItinerariosFormativosDto();
        dto.setId(1L);
        dto.setCodigo("newCodigo");
        dto.setName("testName");
        dto.setUsuario("testUsuario");

        mantenimientoItinerariosFormativosService.update(dto);

        verify(mantenimientoItinerariosFormativosRepository, times(1)).save(existingItinerario);
    }

    @Test
    void testFindAll() {
        ItinerariosFormativos itinerario1 = new ItinerariosFormativos();
        itinerario1.setId(1L);
        ItinerariosFormativos itinerario2 = new ItinerariosFormativos();
        itinerario2.setId(2L);

        when(mantenimientoItinerariosFormativosRepository.findAll()).thenReturn(Arrays.asList(itinerario1, itinerario2));

        List<ItinerariosFormativos> result = mantenimientoItinerariosFormativosService.findAll();

        verify(mantenimientoItinerariosFormativosRepository, times(1)).findAll();
        assert result.size() == 2;
    }

    @Test
    void testDelete() {
        ItinerariosFormativos itinerario = new ItinerariosFormativos();
        itinerario.setId(1L);

        when(mantenimientoItinerariosFormativosRepository.findByid(1L)).thenReturn(itinerario);

        mantenimientoItinerariosFormativosService.delete(1L);

        verify(mantenimientoItinerariosFormativosRepository, times(1)).delete(itinerario);
    }

    @Test
    void testComprobarExistenciaCodigo() {
        ItinerariosFormativos existingItinerario = new ItinerariosFormativos();
        existingItinerario.setCodigo("testCodigo");

        when(mantenimientoItinerariosFormativosRepository.findByCodigo("testCodigo")).thenReturn(existingItinerario);

        try {
            mantenimientoItinerariosFormativosService.comprobarExistenciaCodigo("testCodigo");
        } catch (ItinerarioExistenteException e) {
            assert e.getMessage().contains("testCodigo");
        }

        verify(mantenimientoItinerariosFormativosRepository, times(1)).findByCodigo("testCodigo");
    }
}
