package com.example.flexiMed.notifications.email;

import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

/**
 * REST controller for handling email-related requests via the /api/v1/email endpoint.
 * Provides an endpoint to send emails using the EmailService.
 */
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * Constructs an EmailController with the provided EmailService.
     *
     * @param emailService The EmailService instance used to send emails.
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Endpoint to send an email.
     *

     * @return A string indicating the success or failure of the email sending operation.
     * Returns "Email sent successfully!" on success, or an error message on failure.
     */
    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest request) {
        try {
            Context context = new Context();
            context.setVariables(request.getContextVariables());
            emailService.sendEmail(request.getTo(), request.getSubject(), request.getText(), context);
            return "Email sent successfully!";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}