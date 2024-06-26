package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;

public interface MantenimientoItinerariosFormativosRepository extends JpaRepository<ItinerariosFormativos, Long>{

	ItinerariosFormativos findByid(Long id);
	
	ItinerariosFormativos findByCodigo(String codigo);
	
}
