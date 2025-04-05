package com.example.flexiMed.controllers;

import com.example.flexiMed.controller.OpenAiController;
import com.example.flexiMed.service.TogetherAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the OpenAiController.
 * This class tests the functionality of the OpenAiController, ensuring that it correctly
 * interacts with the TogetherAiService and handles requests for AI chat interactions.
 */
@ExtendWith(MockitoExtension.class)
public class OpenAiControllerTest {

    @Mock
    private TogetherAiService togetherAiService;

    @InjectMocks
    private OpenAiController openAiController;

    private String prompt;
    private String aiResponse;

    /**
     * Sets up the test environment before each test method.
     * Initializes the necessary objects, including a prompt and an AI response.
     */
    @BeforeEach
    void setUp() {
        prompt = "Hello AI, how are you?";
        aiResponse = "I'm doing well, thank you!";
    }

    /**
     * Tests the scenario where a chat request is made to the AI.
     * Verifies that the controller returns the expected AI response.
     */
    @Test
    void chatWithAi_shouldReturnAiResponse() {
        when(togetherAiService.chatWithAi(prompt)).thenReturn(aiResponse);

        String actualResponse = openAiController.chatWithAi(prompt);

        assertEquals(aiResponse, actualResponse);
    }
}