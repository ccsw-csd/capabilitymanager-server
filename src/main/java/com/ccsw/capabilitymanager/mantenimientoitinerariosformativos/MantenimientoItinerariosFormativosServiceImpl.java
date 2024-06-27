package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.certificatesversion.CertificatesVersionRepository;
import com.ccsw.capabilitymanager.certificatesversion.model.CertificatesVersion;
import com.ccsw.capabilitymanager.exception.ItinerarioExistenteException;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MantenimientoItinerariosFormativosServiceImpl implements MantenimientoItinerariosFormativosService{

	@Autowired
	private MantenimientoItinerariosFormativosRepository mantenimientoItinerariosFormativosRepository;
	
	@Override
	public void save(ItinerariosFormativosDto dto) throws ParseException {

		
		comprobarExistenciaCodigo(dto.getCodigo());
		
		ItinerariosFormativos itinerariosFormativo = new ItinerariosFormativos();
		
        Date fechaActual = new Date();
        String fechaMaxBaja = "9999-12-31";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.format(fechaActual);
        Date fechaEspecifica = formatter.parse(fechaMaxBaja);
        
        itinerariosFormativo.setCodigo(dto.getCodigo());
        itinerariosFormativo.setName(dto.getName());
        itinerariosFormativo.setUsuario(dto.getUsuario());
        itinerariosFormativo.setFecha_Alta(fechaActual);
        itinerariosFormativo.setFecha_Baja(fechaEspecifica);

		
		mantenimientoItinerariosFormativosRepository.save(itinerariosFormativo);
	}
	
	@Override
	public void update(ItinerariosFormativosDto dto) throws ParseException {
		
		comprobarExistenciaCodigo(dto.getCodigo());

	    // Find the existing entity by codigo
	    ItinerariosFormativos existingItinerario = mantenimientoItinerariosFormativosRepository.findByid(dto.getId());
	    if (existingItinerario != null) {
	        // Update the fields
	        existingItinerario.setName(dto.getName());
	        existingItinerario.setUsuario(dto.getUsuario());
	        existingItinerario.setCodigo(dto.getCodigo());
	        Date fechaActual = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        formatter.format(fechaActual);
	        existingItinerario.setFecha_Modif(fechaActual);
	        existingItinerario.setUsuario(dto.getUsuario());

	        // Save the updated entity
	        mantenimientoItinerariosFormativosRepository.save(existingItinerario);
	    } else {
	        // Handle case where no entity with given codigo is found
	        throw new RuntimeException("No se encontró ningún itinerario con el código: " + dto.getCodigo());
	    }
	}

	@Override
	public List<ItinerariosFormativos> findAll() {
		return (List<ItinerariosFormativos>) this.mantenimientoItinerariosFormativosRepository.findAll().stream().sorted().toList();
	}

	@Override
	public void delete(Long id) {
		
		ItinerariosFormativos itinerario = mantenimientoItinerariosFormativosRepository.findByid(id);
		
		mantenimientoItinerariosFormativosRepository.delete(itinerario);
		
	}
	
	@Override
	public void comprobarExistenciaCodigo(String codigo) {
		
	    ItinerariosFormativos existingItinerario = mantenimientoItinerariosFormativosRepository.findByCodigo(codigo);

	    if (existingItinerario != null) {
	        // Handle case where a record with the same codigo already exists
	    	 throw new ItinerarioExistenteException(codigo);
	    }
		
	}




}
