package com.ccsw.capabilitymanager.listadobench.controller;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listadobench")
public class ListadoBenchController {

    @Autowired
    private ListadoBenchService service;

    @GetMapping
    public List<ListadoBench> getAllStaffing() {
        return service.getListadoPersonasBench();
    }

    @GetMapping("/{saga}")
    public Optional<List<ListadoBench>> getEmpleadoPorSaga(@PathVariable String saga) {
        return service.getEmpleadoPorSaga(saga);
    }
}
