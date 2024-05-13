package com.ccsw.capabilitymanager.versioncertificados;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.errors.MinioException;

public interface VersionCertificacionesService {
	InputStream recoverFileById(Long id,String fileName)throws IOException, MinioException, InvalidKeyException,
	NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException ;
}
	