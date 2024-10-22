package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import com.ccsw.capabilitymanager.fileprocess.dto.FileProcessDto;
import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.exception.FileProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.time.LocalDateTime;

@Service
public class S3ServiceImpl implements S3Service {
	private static final String ERROR_INIT = ">>> [ERROR][s3ServiceImpl] (";

	@Autowired
	private DataserviceS3 dataservice;

	@Autowired
	private FileProcessRepository fileUploadRepository;

	private static final String ESTADO_CARGADO = "CARGADO";

	/**
	 * Uploads a file to an S3 bucket using MinIO client.
	 * 
	 * <p>This method checks if the specified S3 bucket exists. If the bucket does not exist, it creates a new bucket. Then, it uploads the provided file to the bucket.</p>
	 * 
	 * @param dto The {@link ImportRequestDto} containing the file to be uploaded and additional metadata.
	 * 
	 * <p>The method initializes the MinIO client and performs the following actions:</p>
	 * <ol>
	 *   <li>Checks if the specified bucket exists and creates it if it does not.</li>
	 *   <li>Uploads the file to the specified bucket.</li>
	 * </ol>
	 * 
	 * <p>In case of an error during the process, the method logs the error and sets an appropriate error message in the {@link ImportResponseDto} object.</p>
	 */
	public void uploadFile(ImportRequestDto dto) {

		ImportResponseDto importResponseDto = new ImportResponseDto();
		MinioClient minioClient = dataservice.getMinioClient();
		try {
			// Verificar si el bucket existe, si no, crearlo
			boolean found = minioClient
					.bucketExists(BucketExistsArgs.builder().bucket(dataservice.getBucketName()).build());
			if (!found) {
				CapabilityLogger.logWarning("El bucket no existe, se crea el bucket " + dataservice.getBucketName());
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(dataservice.getBucketName()).build());
			}
			// Subir el archivo al bucket de S3
			String fileName = dto.getFileData().getOriginalFilename();
			minioClient.putObject(PutObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName)
					.stream(dto.getFileData().getInputStream(), dto.getFileData().getSize(), -1).build());

			importResponseDto.setMessage("File uploaded to S3 successfully");
		} catch (Exception e) {
			CapabilityLogger.logError(ERROR_INIT + "uploadFile) : Error subiendo el archivo al bucket de S3.");
			importResponseDto.setError("Error uploading file to S3: " + e.getMessage());
		}
	}

	@Override
	public boolean uploadFile(FileProcessDto dto) {
		MinioClient minioClient = dataservice.getMinioClient();
		boolean result = false;

		try {
			subirFicheroS3(dto);
			guardarFichero(dto);
			result = true;
		} catch (FileProcessException fe) {
			CapabilityLogger.logError(ERROR_INIT + "uploadFile) : Error subiendo el fichero -> " + fe.getMessage());
			result = false;
		}

		return result;
	}

	private void subirFicheroS3(FileProcessDto dto) throws FileProcessException {
		MinioClient minioClient = dataservice.getMinioClient();

		try {
			boolean found = minioClient
					.bucketExists(BucketExistsArgs.builder().bucket(dataservice.getBucketName()).build());
			if (!found) {
				CapabilityLogger.logWarning("El bucket no existe, se crea el bucket " + dataservice.getBucketName());
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(dataservice.getBucketName()).build());
			}
			// Subir el archivo al bucket de S3
			String fileName = dto.getFileData().getOriginalFilename();
			// TODO: subir una version, no machacar el fichero que hay?
			minioClient.putObject(PutObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName)
					.stream(dto.getFileData().getInputStream(), dto.getFileData().getSize(), -1).build());

		} catch (Exception e) {
			throw new FileProcessException("Ha ocurrido un error subiendo el fichero al bucket S3.");
		}
	}

	private void guardarFichero(FileProcessDto dto) throws FileProcessException {
		try {
			FileProcess file = new FileProcess();
			file.setTipoFichero(dto.getTipoFichero());
			file.setEstado(ESTADO_CARGADO);
			file.setNombreFichero(dto.getFileData().getOriginalFilename());
			file.setUsuario(dto.getUsuario());
			file.setFechaImportacion(LocalDateTime.now());
			file.setNombreBucket(dataservice.getBucketName());

			fileUploadRepository.save(file);
		} catch (Exception e) {
			throw new FileProcessException("Ha ocurrido un error guardando los datos del fichero en la base de datos.");
		}
	}
}
