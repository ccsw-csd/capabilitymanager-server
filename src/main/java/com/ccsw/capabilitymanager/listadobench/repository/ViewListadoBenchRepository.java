package com.ccsw.capabilitymanager.listadobench.repository;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ViewListadoBenchRepository {
	Collection<ListadoBench> getListadoPersonasBench();
	Optional<List<ListadoBench>> getEmpleadoPorSaga(String saga); // Nuevo método
	Collection<ListadoBench> getStaffingByDepartment(String ggid);
}