package com.ccsw.capabilitymanager.activity;

import com.ccsw.capabilitymanager.activity.model.Activity;
import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.common.exception.UnprocessableEntityException;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;


import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
	private static final String ERROR_INIT = ">>> [ERROR][ActivityServiceImpl] (";
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Saves an activity after validating the data.
     *
     * @param activityDTO The ActivityDTO object containing the activity details to be saved.
     * @throws UnprocessableEntityException if the end date is before the start date.
     */
    @Override
    public void save(ActivityDTO activityDTO) {
            Activity activity = new Activity();
            BeanUtils.copyProperties(activityDTO, activity, "id");
            if (activity.getFechaFinalizacion() != null && activity.getFechaFinalizacion().before(activity.getFechaInicio())) {
                String respuestaEr = ERROR_INIT +"save): Error al guardar la actividad. La fecha de finalización tiene que ser mayor o igual a la de inicio";
                CapabilityLogger.logError(respuestaEr);
                throw new UnprocessableEntityException(respuestaEr);
            }

        this.activityRepository.save(activity);
    }
    
    /**
     * Retrieves a list of all activities.
     *
     * @return A list of {@link Activity} objects representing all activities in the repository.
     */
    @Override
    public List<Activity> findAll() {
        return this.activityRepository.findAll();
    }

    /**
     * Retrieves a list of activities filtered by a specific GGID.
     *
     * @param ggid The GGID used to filter the activities.
     * @return A list of {@link Activity} objects associated with the given GGID.
     */
    @Override
    public List<Activity> findByGgid(String ggid) {
        return activityRepository.findByGgid(ggid).stream().toList();
    }

    /**
     * Retrieves a list of activities filtered by a specific saga.
     *
     * @param saga The saga used to filter the activities.
     * @return A list of {@link Activity} objects associated with the given saga.
     */
    public List<Activity> findBySaga(String saga) {
        return activityRepository.findBySaga(saga).stream().toList();
    }

    /**
	 * Deletes an {@link Activity} entity by its ID.
	 *
	 * <p>This method performs the following actions:</p>
	 * <ul>
	 *   <li>Finds the {@link Activity} entity with the specified ID using {@link activityRepository#findByid(Long)}.</li>
	 *   <li>Deletes the found entity from the repository using {@link activityRepository#delete(activity)}.</li>
	 * </ul>
	 *
	 * @param id The ID of the {@link Activity} entity to be deleted.
	 * @throws EntityNotFoundException If no entity with the specified ID is found.
	 */
	@Override
	public void delete(Long id) {
		Activity activity = activityRepository.findByid(id);
		
		activityRepository.delete(activity);
	}

    /**
     * Updates an existing {@link Activity} entity using the provided {@link ActivityDTO}.
     *
     * This method performs the following actions:
     * Finds the existing {@link Activity} entity by its ID using {@link activityRepository#findById(Long)}.
     * Updates the fields of the existing activity with values from the provided {@link ActivityDTO}:
     * Updates the activity code, name, state, start date, end date, progress percentage, observations, saga, and ggid.
     * If the activity type ID has changed, updates the activity type ID as well.
     * Sets the last activity date to the current date.
     * Saves the updated entity back to the repository using {@link activityRepository#save(activity)}.
     *
     *
     * @param dto The {@link ActivityDTO} containing the updated values for the {@link Activity} entity.
     * @throws ParseException If there is an error parsing dates during the update process.
     */
    @Override
    public void update(ActivityDTO dto) throws ParseException{

        // Buscar la actividad existente por el ID (devuelve un Optional)
        Optional<Activity> optionalActivity = activityRepository.findById(dto.getId());

        // Obtener la actividad existente
        Activity existingActivity = optionalActivity.get();

        // Actualizar los campos de la actividad
        existingActivity.setCodigoActividad(dto.getCodigoActividad());
        existingActivity.setNombreActividad(dto.getNombreActividad());
        existingActivity.setEstado(dto.getEstado());
        existingActivity.setFechaInicio(dto.getFechaInicio());
        existingActivity.setFechaFinalizacion(dto.getFechaFinalizacion());
        existingActivity.setPorcentajeAvance(dto.getPorcentajeAvance());
        existingActivity.setObservaciones(dto.getObservaciones());
        existingActivity.setSaga(dto.getSaga());
        existingActivity.setGgid(dto.getGgid());

        // Si se cambió el tipo de actividad, actualizamos también
        if (!dto.getTipoActividadId().equals(existingActivity.getTipoActividadId())) {
            existingActivity.setTipoActividadId(dto.getTipoActividadId());
        }

        // Actualizamos la fecha de la última modificación a la fecha actual
        Date fechaActual = new Date();
        existingActivity.setFechaUltimaActividad(fechaActual);

        // Guardar la actividad actualizada
        activityRepository.save(existingActivity);
    }

}
