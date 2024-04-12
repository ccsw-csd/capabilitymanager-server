package com.ccsw.dashboard.listadobench.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.listadobench.model.ListadoBench;
import com.ccsw.dashboard.listadobench.repository.ListadoBenchRepository;
import com.ccsw.dashboard.listadobench.service.ListadoBenchService;

@Service
public class ListadoBenchServiceImpl implements ListadoBenchService {

	@Autowired
	private ListadoBenchRepository listadoBenchRepository;

	@Override
	public List<ListadoBench> getListadoPersonasBench() {
	
		   List<String> status = Arrays.asList(Constants.BDCS_STATUS, Constants.DISPONIBLE_STATUS, Constants.EN_FORMACION_STATUS);
		
		return (List<ListadoBench>) listadoBenchRepository.findByStatusIn(status);
				}
	

}
