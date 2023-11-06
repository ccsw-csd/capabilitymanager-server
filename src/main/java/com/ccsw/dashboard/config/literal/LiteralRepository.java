package com.ccsw.dashboard.config.literal;

import com.ccsw.dashboard.config.literal.model.Literal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface LiteralRepository extends CrudRepository<Literal, Long>, JpaRepository<Literal, Long> {

	Optional<Literal> findById(Long id);
	List<Literal> findByType(String type);
	List<Literal> findByTypeAndSubtype(String type, String subtype);
	List<Literal> findAll();
	
}
