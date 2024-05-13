package com.ccsw.capabilitymanager.listadobench.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.listadobench.model.ListadoBench;
import com.ccsw.capabilitymanager.listadobench.service.ListadoBenchService;

@RestController
@RequestMapping("/listadobench")
public class ListadoBenchController {

	@Autowired
	private ListadoBenchService service;

	@GetMapping("/datos")
	public List<ListadoBench> getAllStaffing() {
		return service.getListadoPersonasBench();
	}
}
