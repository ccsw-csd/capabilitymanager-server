package com.ccsw.capabilitymanager.versionitinerarios;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.errors.MinioException;

public interface VersionItinerariosService {
	InputStream recoverFileById(Long id,String fileName)throws IOException, MinioException, InvalidKeyException,
	NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException ;
}
	