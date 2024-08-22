package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.exception.ItinerarioExistenteException;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativos;
import com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model.ItinerariosFormativosDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MantenimientoItinerariosFormativosServiceImpl implements MantenimientoItinerariosFormativosService {
	private static final String ERROR_INIT = ">>> [ERROR][MantenimientoItinerariosFormativosServiceImpl] (";
	@Autowired
	private MantenimientoItinerariosFormativosRepository mantenimientoItinerariosFormativosRepository;

	/**
	 * Saves a new {@link ItinerariosFormativos} entity based on the provided DTO.
	 *
	 * <p>This method performs the following actions:</p>
	 * <ul>
	 *   <li>Checks if an entry with the given code already exists by calling {@link #comprobarExistenciaCodigo(String)}.</li>
	 *   <li>Creates a new {@link ItinerariosFormativos} entity and sets its properties based on the provided {@link ItinerariosFormativosDto}.</li>
	 *   <li>Sets the current date as the 'fecha de alta' and a maximum future date ("9999-12-31") as the 'fecha de baja'.</li>
	 *   <li>Persists the new entity in the repository using {@link mantenimientoItinerariosFormativosRepository#save(ItinerariosFormativos)}.</li>
	 * </ul>
	 *
	 * @param dto The {@link ItinerariosFormativosDto} containing the data to be saved.
	 * @throws ParseException If an error occurs while parsing the date.
	 */
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

	/**
	 * Updates an existing {@link ItinerariosFormativos} entity with the data from the provided DTO.
	 *
	 * <p>This method performs the following actions:</p>
	 * <ul>
	 *   <li>Finds the existing {@link ItinerariosFormativos} entity by its ID.</li>
	 *   <li>If the code in the DTO is different from the existing entity's code, it checks for the existence of the new code by calling {@link #comprobarExistenciaCodigo(String)}.</li>
	 *   <li>Updates the fields of the existing entity with the values from the DTO.</li>
	 *   <li>Sets the current date as the 'fecha de modificación'.</li>
	 *   <li>Saves the updated entity back to the repository using {@link mantenimientoItinerariosFormativosRepository#save(ItinerariosFormativos)}.</li>
	 * </ul>
	 *
	 * @param dto The {@link ItinerariosFormativosDto} containing the updated data.
	 * @throws ParseException If an error occurs while formatting the date (although formatting is not required here as the current date is used directly).
	 */
	@Override
	public void update(ItinerariosFormativosDto dto) throws ParseException {
		// Find the existing entity by codigo
		ItinerariosFormativos existingItinerario = mantenimientoItinerariosFormativosRepository.findByid(dto.getId());
		
		if(!dto.getCodigo().equals(existingItinerario.getCodigo())) {
	   //Comprobar existencia de codigo
		comprobarExistenciaCodigo(dto.getCodigo());
		}
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

	}

	/**
	 * Retrieves all {@link ItinerariosFormativos} entities from the repository.
	 *
	 * <p>This method fetches all entities from the repository and sorts them in their natural order.</p>
	 *
	 * @return A {@link List} of {@link ItinerariosFormativos} entities, sorted in natural order.
	 */
	@Override
	public List<ItinerariosFormativos> findAll() {
		return (List<ItinerariosFormativos>) this.mantenimientoItinerariosFormativosRepository.findAll().stream()
				.sorted().toList();
	}

	/**
	 * Deletes an {@link ItinerariosFormativos} entity by its ID.
	 *
	 * <p>This method performs the following actions:</p>
	 * <ul>
	 *   <li>Finds the {@link ItinerariosFormativos} entity with the specified ID using {@link mantenimientoItinerariosFormativosRepository#findByid(Long)}.</li>
	 *   <li>Deletes the found entity from the repository using {@link mantenimientoItinerariosFormativosRepository#delete(ItinerariosFormativos)}.</li>
	 * </ul>
	 *
	 * @param id The ID of the {@link ItinerariosFormativos} entity to be deleted.
	 * @throws EntityNotFoundException If no entity with the specified ID is found.
	 */
	@Override
	public void delete(Long id) {

		ItinerariosFormativos itinerario = mantenimientoItinerariosFormativosRepository.findByid(id);

		mantenimientoItinerariosFormativosRepository.delete(itinerario);

	}

	/**
	 * Checks if an {@link ItinerariosFormativos} entity with the specified code already exists.
	 *
	 * <p>This method queries the repository to determine if an entity with the provided code exists. If an entity with
	 * the same code is found, an {@link ItinerarioExistenteException} is thrown to indicate a conflict.</p>
	 *
	 * @param codigo The code to check for existence.
	 * @throws ItinerarioExistenteException If an entity with the specified code already exists.
	 */
	@Override
	public void comprobarExistenciaCodigo(String codigo) {

		ItinerariosFormativos existingItinerario = mantenimientoItinerariosFormativosRepository.findByCodigo(codigo);

		if (existingItinerario != null) {
			// Handle case where a record with the same codigo already exists
			CapabilityLogger.logError(ERROR_INIT + "comprobarExistenciaCodigo):Existe un itinerario con el mismo código");
			throw new ItinerarioExistenteException(codigo);
		}

	}

}
