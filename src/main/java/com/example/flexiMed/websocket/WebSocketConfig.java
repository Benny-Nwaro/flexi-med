package com.example.flexiMed.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/ambulance-updates")
                .setHandshakeHandler(new UserIdHandshakeHandler())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue", "/topic")
                .setHeartbeatValue(new long[]{10000, 10000});
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler defaultTaskScheduler = new ThreadPoolTaskScheduler();
        defaultTaskScheduler.setPoolSize(1); // You can adjust this
        defaultTaskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        defaultTaskScheduler.initialize();
        return defaultTaskScheduler;
    }
}