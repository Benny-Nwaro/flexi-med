package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ambulance-updates")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new UserIdHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        String rabbitmqUrl = System.getenv("CLOUDAMQP_URL");
        if (rabbitmqUrl != null) {
            // Parse the RabbitMQ URL to get host, port, username, and password
            java.net.URI uri = java.net.URI.create(rabbitmqUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            if (port == -1 && uri.getScheme().equals("amqps")) {
                port = 5671; // Default AMQPS port
                logger.info("Using default AMQPS port: 5671");
            }

            String username = uri.getUserInfo().split(":")[0];
            String password = uri.getUserInfo().split(":")[1];

            registry.enableStompBrokerRelay("/topic", "/queue")
                    .setRelayHost(host)
                    .setRelayPort(port)
                    .setClientLogin(username)
                    .setClientPasscode(password);
            registry.setApplicationDestinationPrefixes("/app");
            registry.setUserDestinationPrefix("/user");

            logger.info("STOMP Broker Relay configured with host: {}, port: {}, user: {}", host, port, username);

        } else {
            // Fallback to simple broker if CLOUDAMQP_URL is not available (e.g., in local development)
            registry.enableSimpleBroker("/topic", "/queue");
            registry.setApplicationDestinationPrefixes("/app");
            registry.setUserDestinationPrefix("/user");
            System.err.println("Warning: CLOUDAMQP_URL environment variable not found. Using Simple Broker (not recommended for production).");
        }
    }
}