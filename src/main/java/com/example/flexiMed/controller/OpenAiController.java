package com.example.flexiMed.controller;

import com.example.flexiMed.service.TogetherAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for interacting with an AI service, specifically for chat functionality.
 * It enables users to send a prompt to the AI and receive a response.
 */
@RestController
@RequestMapping("/api/ai")
public class OpenAiController {

    private final TogetherAiService togetherAiService;

    /**
     * Constructor to initialize the OpenAiController with the given TogetherAiService.
     *
     * @param togetherAiService The service responsible for interacting with the AI.
     */
    public OpenAiController(TogetherAiService togetherAiService) {
        this.togetherAiService = togetherAiService;
    }

    /**
     * Endpoint for chatting with the AI.
     * The user sends a prompt, and the AI responds with a generated response.
     *
     * @param prompt The input prompt to send to the AI.
     * @return A string containing the AI's response to the prompt.
     */
    @GetMapping("/chat")
    public String chatWithAi(@RequestParam String prompt) {
        return togetherAiService.chatWithAi(prompt);
    }
}
