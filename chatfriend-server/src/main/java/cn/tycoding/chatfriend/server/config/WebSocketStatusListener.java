package cn.tycoding.chatfriend.server.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketStatusListener {

    private final SimpMessageSendingOperations messagingTemplate;
    // Simple in-memory map representing online status
    public static final Map<String, Boolean> onlineUsers = new ConcurrentHashMap<>();

    public WebSocketStatusListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionUserId = (String) headerAccessor.getSessionAttributes().get("userId");
        if (sessionUserId != null) {
            onlineUsers.put(sessionUserId, true);
            messagingTemplate.convertAndSend("/topic/status", sessionUserId + " is online");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionUserId = (String) headerAccessor.getSessionAttributes().get("userId");
        if (sessionUserId != null) {
            onlineUsers.remove(sessionUserId);
            messagingTemplate.convertAndSend("/topic/status", sessionUserId + " is offline");
        }
    }
}
