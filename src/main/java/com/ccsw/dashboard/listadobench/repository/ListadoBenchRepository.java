package com.ccsw.dashboard.listadobench.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccsw.dashboard.listadobench.model.ListadoBench;

public interface ListadoBenchRepository extends JpaRepository<ListadoBench,Long> {
	  List<ListadoBench> findByStatusIn(List<String> status);
}
