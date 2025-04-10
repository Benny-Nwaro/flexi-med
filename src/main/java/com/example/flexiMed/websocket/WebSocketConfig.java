package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ambulance-updates")
                .setHandshakeHandler(new UserIdHandshakeHandler()) // 👈 Inject custom handler
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // For sending messages from client
        registry.enableSimpleBroker("/queue", "/topic");    // For broadcasting messages
        registry.setUserDestinationPrefix("/user");         // 👈 Needed for convertAndSendToUser
    }
}
