package com.ccsw.capabilitymanager.config.literal;

import com.ccsw.capabilitymanager.config.literal.model.Literal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface LiteralRepository extends JpaRepository<Literal, Long> {

	Optional<Literal> findById(Long id);
	List<Literal> findByType(String type);
	List<Literal> findBySubtype(String subtype);
	List<Literal> findByTypeAndSubtype(String type, String subtype);
	List<Literal> findAll();
	
}
