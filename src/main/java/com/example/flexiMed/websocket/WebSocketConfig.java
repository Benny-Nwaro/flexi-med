package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;

/**
 * Configuration class for WebSocket messaging using STOMP.
 * This class enables and configures WebSocket message broker capabilities for the application.
 * <p>
 * It sets up the STOMP protocol for WebSocket communication, defines the endpoints
 * clients can connect to, and configures the message broker to handle routing
 * of messages.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Declares a bean for the custom HandshakeHandler.
     * This allows for customizing the WebSocket handshake process, in this case, to extract
     * the user ID from the request.
     *
     * @return A {@link HandshakeHandler} instance, specifically a {@link UserIdHandshakeHandler}.
     */
    @Bean
    public HandshakeHandler handshakeHandler() {
        return new UserIdHandshakeHandler(); // Use your custom handler
    }

    /**
     * Registers STOMP endpoints for WebSocket connections.
     * <p>
     * Clients will use these endpoints to establish a WebSocket connection with the server.
     * SockJS is enabled as a fallback option for browsers that do not fully support WebSockets.
     *
     * @param registry The {@link StompEndpointRegistry} used to register STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ambulance-updates")
                .setHandshakeHandler(handshakeHandler()) // Use the custom handshake handler.
                .setAllowedOriginPatterns("*") // Allows connections from any origin.  Consider restricting this in production.
                .withSockJS(); // Enable SockJS fallback.
    }

    /**
     * Configures the message broker for handling messages.
     * <p>
     * Configures how messages are routed within the application.  Uses a simple in-memory broker.
     * Also sets the application and user destination prefixes.
     *
     * @param registry The {@link MessageBrokerRegistry} used to configure the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); //  Enable a simple message broker for /topic and /queue destinations.
        registry.setApplicationDestinationPrefixes("/app"); //  Messages destined for @Controller methods should start with /app.
        registry.setUserDestinationPrefix("/user");   //  Messages destined for a specific user should start with /user.
    }
}
