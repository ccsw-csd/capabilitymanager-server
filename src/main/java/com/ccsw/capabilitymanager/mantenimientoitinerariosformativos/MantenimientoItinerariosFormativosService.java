package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import java.text.ParseException;
import java.util.List;

import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;


public interface MantenimientoItinerariosFormativosService {

	List<ItinerariosFormativos> findAll();
	
	void save(ItinerariosFormativosDto dto) throws ParseException;
	
	void delete(Long id);

	void update(ItinerariosFormativosDto dto) throws ParseException;

	void comprobarExistenciaCodigo(String codigo);
}
