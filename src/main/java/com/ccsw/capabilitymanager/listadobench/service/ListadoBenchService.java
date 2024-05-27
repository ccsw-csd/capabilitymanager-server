package com.ccsw.capabilitymanager.listadobench.service;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ListadoBenchService {
    List<ListadoBench> getListadoPersonasBench();

    Optional<List<ListadoBench>> getEmpleadoPorSaga(String saga);  // Nuevo método
}
