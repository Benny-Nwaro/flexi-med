package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket messaging using STOMP.
 * This class enables and configures WebSocket message broker capabilities for the application.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints for WebSocket connections.
     *
     * @param registry The StompEndpointRegistry used to register STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws", "/ws/ambulance-updates")
                .setAllowedOrigins("http://localhost:3000", "https://flexi-med-front-itcp.vercel.app")
                .withSockJS();
    }

    /**
     * Configures the message broker for handling messages.
     *
     * @param registry The MessageBrokerRegistry used to configure the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}