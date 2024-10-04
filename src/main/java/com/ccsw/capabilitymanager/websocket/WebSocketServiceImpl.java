package com.ccsw.capabilitymanager.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service("webSocketService")
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyClient(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}