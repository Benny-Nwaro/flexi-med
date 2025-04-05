package com.example.flexiMed.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles WebSocket communication for ambulance location updates.
 * This class extends TextWebSocketHandler to manage WebSocket sessions and send location data to specific users.
 */
@Service
public class AmbulanceLocationHandler extends TextWebSocketHandler {

    /**
     * A thread-safe map to store WebSocket sessions associated with user IDs.
     */
    private final ConcurrentHashMap<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /**
     * Called after a WebSocket connection has been established.
     * Stores the WebSocket session associated with the user ID extracted from the session.
     *
     * @param session The WebSocketSession representing the connection.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        userSessions.put(userId, session);
    }

    /**
     * Handles incoming text messages from WebSocket clients.
     * This implementation does not process incoming messages.
     *
     * @param session The WebSocketSession from which the message was received.
     * @param message The TextMessage received.
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // No need to handle incoming messages
    }

    /**
     * Called after a WebSocket connection has been closed.
     * Removes the WebSocket session associated with the user ID.
     *
     * @param session The WebSocketSession that was closed.
     * @param status  The CloseStatus indicating the reason for the closure.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserIdFromSession(session);
        userSessions.remove(userId);
    }

    /**
     * Sends a location update to a specific user.
     *
     * @param userId       The ID of the user to send the location update to.
     * @param locationJson The location data in JSON format.
     */
    public void sendLocationToUser(String userId, String locationJson) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(locationJson));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Extracts the user ID from the WebSocket session's query parameters.
     *
     * @param session The WebSocketSession.
     * @return The user ID extracted from the query parameters, or "unknown" if not found.
     */
    private String getUserIdFromSession(WebSocketSession session) {
        // Extract user ID from query params (e.g., ws://localhost:8080/ws/ambulance-updates?userId=USER123)
        String query = session.getUri().getQuery();
        return query != null && query.startsWith("userId=") ? query.split("=")[1] : "unknown";
    }
}