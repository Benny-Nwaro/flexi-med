package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for STOMP-based WebSocket connections used for real-time ambulance tracking.
 * This works alongside other STOMP-based messaging for a unified setup.
 */
@Configuration
@EnableWebSocketMessageBroker
public class AmbulanceWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers a STOMP endpoint for ambulance tracking WebSocket connections.
     *
     * @param registry the StompEndpointRegistry to register STOMP endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ambulance-locations")
                .setAllowedOrigins("https://flexi-med-front-itcp.vercel.app")
                .withSockJS();
    }

    /**
     * Configures the message broker for ambulance location updates.
     *
     * @param registry the MessageBrokerRegistry to configure message routing
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/user"); // Enable user destination prefix
        registry.setApplicationDestinationPrefixes("/app");
    }
}
