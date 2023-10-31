package com.ccsw.dashboard.config.literal;

import com.ccsw.dashboard.config.literal.model.Literal;

import java.util.List;

public interface LiteralService {

	Literal findById(Long id);
	List<Literal> findByType(String type);
	List<Literal> findByTypeAndSubtype(String type, String subtype);
	List<Literal> findAll();	
	
}
