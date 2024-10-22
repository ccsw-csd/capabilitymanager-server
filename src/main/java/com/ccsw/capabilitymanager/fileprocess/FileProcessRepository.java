package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileProcessRepository extends JpaRepository<FileProcess, Long> {

    List<FileProcess> findByEstado(String estado);

}
