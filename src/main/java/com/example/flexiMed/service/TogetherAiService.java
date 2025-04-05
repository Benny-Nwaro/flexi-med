package com.example.flexiMed.service;

import com.example.flexiMed.exceptions.ErrorResponse.AiApiRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * Service class for interacting with the Together AI API.
 * This class handles sending chat prompts to the AI model and retrieving responses.
 */
@Service
public class TogetherAiService {

    /**
     * API key for authenticating requests to the Together AI API.
     * Retrieved from application properties.
     */
    @Value("${TOGETHER_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;

    /**
     * Constructor for TogetherAiService.
     * @param restTemplate RestTemplate for making HTTP requests.
     */
    public TogetherAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends a chat prompt to the AI model and retrieves a response.
     * @param prompt The user-provided message to send to the AI.
     * @return The AI-generated response as a String.
     * @throws AiApiRequestException if the API request fails.
     */
    public String chatWithAi(String prompt) {
        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = createRequestBody(prompt);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        String API_URL = "https://api.together.xyz/v1/chat/completions";
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new AiApiRequestException("Failed to get valid response from AI API. Status code: " + response.getStatusCode());
        }
    }

    /**
     * Creates HTTP headers required for the API request.
     * @return HttpHeaders object with authorization and content type set.
     */
    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Constructs the request body for the AI API request.
     * @param prompt The user message to send to the AI.
     * @return A Map representing the JSON request body.
     */
    public Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 200);
        return requestBody;
    }
}
