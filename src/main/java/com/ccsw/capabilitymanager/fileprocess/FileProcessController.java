package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.fileprocess.dto.FileProcessDto;
import com.ccsw.capabilitymanager.fileprocess.dto.FileProcessResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/upload")
@RestController
public class FileProcessController {

    @Autowired
    private S3Service s3Service;

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public FileProcessResponseDto uploadFile(@RequestPart("fileUploadDto") @ModelAttribute FileProcessDto dto) {
        FileProcessResponseDto response = new FileProcessResponseDto();
        response.setFilename(dto.getNombreFichero());

        if(s3Service.uploadFile(dto)) {
            response.setMessage("Se ha guardado el fichero y se comenzará a procesar en segundo plano.");
        } else {
            response.setError("Ha ocurrido algún problema guardando el fichero.");
        }

        return response;
    }

}