package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;

import java.util.concurrent.CompletableFuture;

public interface FileProcessService {

    public CompletableFuture<Void> processPendingFile(FileProcess file) throws Exception;

}
