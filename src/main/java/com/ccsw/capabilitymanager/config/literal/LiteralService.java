package com.ccsw.capabilitymanager.config.literal;

import com.ccsw.capabilitymanager.config.literal.model.Literal;

import java.util.List;

public interface LiteralService {

	Literal findById(Long id);
	List<Literal> findByType(String type);
	List<Literal> findBySubtype(String subtype);
	List<Literal> findByTypeAndSubtype(String type, String subtype);
	List<Literal> findAll();	
	
}
