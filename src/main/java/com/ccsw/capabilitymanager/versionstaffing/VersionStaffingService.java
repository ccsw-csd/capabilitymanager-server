package com.ccsw.capabilitymanager.versionstaffing;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.errors.MinioException;


public interface VersionStaffingService {
	InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException;
}
