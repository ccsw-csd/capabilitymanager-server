package com.ccsw.capabilitymanager.certificatesdataimport;

import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataEnCursoImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CertificatesDataEnCursoImportRepository extends JpaRepository<CertificatesDataEnCursoImport, Long> {
	
	//TODO: implementar este campo en la clase del modelo y en bbdd para que luego se puedan eliminar los certificados
	 //@Query("SELECT c FROM CertificatesDataEnCursoImport c WHERE c.numImportCodeId = :numImportCodeId")
	 //   List<CertificatesDataEnCursoImport> findByNumImportCodeId(@Param("numImportCodeId") Long numImportCodeId);
}
