package com.ccsw.dashboard.listadobench.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.listadobench.model.ListadoBench;
import com.ccsw.dashboard.listadobench.repository.ViewListadoBenchRepository;
import com.ccsw.dashboard.listadobench.service.ListadoBenchService;

@Service
public class ListadoBenchServiceImpl implements ListadoBenchService {

	@Autowired
	private ViewListadoBenchRepository listadoBenchRepository;

	@Override
	public List<ListadoBench> getListadoPersonasBench() {
	
		return (List<ListadoBench>) listadoBenchRepository.getListadoPersonasBench();
	}
	

}
