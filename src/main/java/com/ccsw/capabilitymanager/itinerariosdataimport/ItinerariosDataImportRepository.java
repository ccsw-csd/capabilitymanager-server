package com.ccsw.capabilitymanager.itinerariosdataimport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccsw.capabilitymanager.itinerariosdataimport.model.ItinerariosDataImport;

@Repository
@Transactional
public interface ItinerariosDataImportRepository extends JpaRepository<ItinerariosDataImport, Long> {


}
