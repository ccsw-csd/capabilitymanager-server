package com.ccsw.dashboard.listadobench.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ccsw.dashboard.listadobench.model.ListadoBench;

@Service
public interface ListadoBenchService {

	public List<ListadoBench> getListadoPersonasBench();

}
