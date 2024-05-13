package com.ccsw.capabilitymanager.listadobench.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;

@Service
public interface ListadoBenchService {

	public List<ListadoBench> getListadoPersonasBench();

}
