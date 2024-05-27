package com.ccsw.capabilitymanager.listadobench.service.impl;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.repository.ViewListadoBenchRepository;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListadoBenchServiceImpl implements ListadoBenchService {

    @Autowired
    private ViewListadoBenchRepository listadoBenchRepository;

    @Override
    public List<ListadoBench> getListadoPersonasBench() {
        return (List<ListadoBench>) listadoBenchRepository.getListadoPersonasBench();
    }

    @Override
    public Optional<List<ListadoBench>> getEmpleadoPorSaga(String saga) {
        return listadoBenchRepository.getEmpleadoPorSaga(saga);
    }
}


