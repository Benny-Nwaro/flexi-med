package com.example.flexiMed.notifications.sms;

import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling SMS-related requests via the /api/v1/sms endpoint.
 * Provides an endpoint to send SMS messages using the SMSService.
 */
@RestController
@RequestMapping("/api/v1/sms")
public class SMSController {

    private final SMSService smsService;

    /**
     * Constructs an SMSController with the provided SMSService.
     *
     * @param smsService The SMSService instance used to send SMS messages.
     */
    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

    /**
     * Endpoint to send an SMS message.
     *
     * @param to      The recipient's phone number (in E.164 format).
     * @param message The message body to send.
     * @return A string indicating the success or failure of the SMS sending operation.
     * Returns "SMS sent successfully!" on success, or an error message on failure.
     */
    @PostMapping("/send")
    public String sendSms(@RequestParam String to, @RequestParam String message) {
        try {
            smsService.sendSms(to, message);
            return "SMS sent successfully!";
        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }
}