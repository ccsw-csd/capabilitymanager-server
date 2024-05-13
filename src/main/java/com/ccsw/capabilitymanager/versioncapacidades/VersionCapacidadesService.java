package com.ccsw.capabilitymanager.versioncapacidades;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.errors.MinioException;

public interface VersionCapacidadesService {
	InputStream recoverFileById(Long id,String fileName)throws IOException, MinioException, InvalidKeyException,
	NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException ;
}
