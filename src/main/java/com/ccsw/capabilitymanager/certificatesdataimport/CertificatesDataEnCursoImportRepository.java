package com.ccsw.capabilitymanager.certificatesdataimport;

import com.ccsw.capabilitymanager.certificatesdataimport.model.CertificatesDataEnCursoImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for managing CertificatesDataEnCursoImport entities. Extends JpaRepository to provide CRUD
 * operations.
 */
@Repository
@Transactional
public interface CertificatesDataEnCursoImportRepository extends JpaRepository<CertificatesDataEnCursoImport, Long> {

  /**
   * Finds a list of CertificatesDataEnCursoImport entities by their ID.
   *
   * @param id the ID of the CertificatesDataEnCursoImport entities to find
   * @return a list of CertificatesDataEnCursoImport entities with the specified ID
   */
  @Query("SELECT c FROM CertificatesDataEnCursoImport c WHERE c.id = :id")
  List<CertificatesDataEnCursoImport> findByIdCertificatesDataEnCursoImports(@Param("id") Long id);
}
