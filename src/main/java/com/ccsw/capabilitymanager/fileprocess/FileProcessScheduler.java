package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import com.ccsw.capabilitymanager.fileprocess.model.FileProcess;
import com.ccsw.capabilitymanager.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileProcessScheduler {

    private final static String ESTADO_CARGADO = "CARGADO";

    @Autowired
    private FileProcessService fileProcessService;

    @Autowired
    private FileProcessRepository fileProcessRepository;

    @Autowired
    private WebSocketService webSocketService;

    //@Scheduled(fixedRate = 60000)
    @Scheduled(fixedRate = 6000)
    public void processPendingFiles() {
        List<FileProcess> filesPending = fileProcessRepository.findByEstado(ESTADO_CARGADO);

        if(!filesPending.isEmpty()) {
            for(FileProcess file : filesPending) {
                try {
                    CompletableFuture<Void> future = fileProcessService.processPendingFile(file);

                    future.thenRun(() -> {
                        CapabilityLogger.logInfo("ASYNC OK");
                        // Enviar un mensaje al frontend cuando el procesamiento haya finalizado
                        String message = "El fichero " + file.getNombreFichero() + " ha sido procesado correctamente.";
                        webSocketService.notifyClient(message);
                    }).exceptionally(ex -> {
                        CapabilityLogger.logError(ex.getMessage());
                        webSocketService.notifyClient(ex.getMessage());

                        return null;
                    });
                } catch (Exception e) {
                	String message = "Ha ocurrido un error procesando el fichero " + file.getNombreFichero();
                    CapabilityLogger.logError(message);
                    webSocketService.notifyClient(message);
                }
            }
        }

    }

}
