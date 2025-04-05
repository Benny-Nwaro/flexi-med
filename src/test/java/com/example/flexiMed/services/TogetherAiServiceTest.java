package com.example.flexiMed.services;

import com.example.flexiMed.exceptions.ErrorResponse.AiApiRequestException;
import com.example.flexiMed.service.TogetherAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TogetherAiService.
 * This class tests the functionality of the TogetherAiService, ensuring that it correctly
 * interacts with the RestTemplate and handles various scenarios related to communicating
 * with the Together AI API.
 */
@ExtendWith(MockitoExtension.class)
public class TogetherAiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TogetherAiService togetherAiService;

    @Value("${TOGETHER_API_KEY}")
    private String apiKey;

    private String prompt;
    private String apiUrl;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects and mocks, including the API key, prompt, and API URL.
     */
    @BeforeEach
    void setUp() {
        apiKey = "testApiKey"; // Mocking the API key
        togetherAiService = new TogetherAiService(restTemplate);
        prompt = "Hello AI, how are you?";
        apiUrl = "https://api.together.xyz/v1/chat/completions";
    }

    /**
     * Tests the scenario where the AI API call is successful.
     * Verifies that the service returns the expected AI response.
     */
    @Test
    void chatWithAi_shouldReturnAiResponse_whenApiCallIsSuccessful() {
        String expectedResponse = "{\"response\": \"I am doing well!\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        String actualResponse = togetherAiService.chatWithAi(prompt);

        assertEquals(expectedResponse, actualResponse);
    }

    /**
     * Tests the scenario where the AI API call fails.
     * Verifies that the service throws an AiApiRequestException.
     */
    @Test
    void chatWithAi_shouldThrowAiApiRequestException_whenApiCallFails() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);

        when(restTemplate.postForEntity(eq(apiUrl), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        assertThrows(AiApiRequestException.class, () -> togetherAiService.chatWithAi(prompt));
    }

    /**
     * Tests the creation of the request body for the AI API call.
     * Verifies that the request body contains the correct data, including the model, messages, and max tokens.
     */
    @Test
    void createRequestBody_shouldContainCorrectData() {
        Map<String, Object> requestBody = togetherAiService.createRequestBody(prompt);

        assertEquals("mistralai/Mixtral-8x7B-Instruct-v0.1", requestBody.get("model"));
        List<Map<String, String>> messages = (List<Map<String, String>>) requestBody.get("messages");
        assertEquals(1, messages.size());
        assertEquals("user", messages.get(0).get("role"));
        assertEquals(prompt, messages.get(0).get("content"));
        assertEquals(200, requestBody.get("max_tokens"));
    }
}