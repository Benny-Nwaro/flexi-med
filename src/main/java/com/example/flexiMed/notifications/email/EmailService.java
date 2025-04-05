package com.example.flexiMed.notifications.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Service class for sending emails using Spring Mail and Thymeleaf templates.
 * This class handles email sending with HTML content generated from Thymeleaf templates.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * The sender's email address, injected from application properties.
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Constructs an EmailService with the provided JavaMailSender and TemplateEngine.
     *
     * @param mailSender     The JavaMailSender instance used to send emails.
     * @param templateEngine The TemplateEngine instance used to process Thymeleaf templates.
     */
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends an email with HTML content generated from a Thymeleaf template.
     *
     * @param to           The recipient's email address.
     * @param subject      The email subject.
     * @param templateName The name of the Thymeleaf template to process.
     * @param context      The Thymeleaf context containing variables for the template.
     * @throws MessagingException       If an error occurs while sending the email.
     * @throws IllegalArgumentException If the provided email address is invalid.
     */
    public void sendEmail(String to, String subject, String templateName, Context context) throws MessagingException {
        if (!isValidEmail(to)) {
            logger.error("Invalid email address: {}", to);
            throw new IllegalArgumentException("Invalid email address");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            String htmlContent = templateEngine.process(templateName, context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}", to, e);
            throw e;  // Re-throw the exception after logging it
        }
    }

    /**
     * Checks if the provided email address is valid.
     *
     * @param email The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        // Basic email validation (you can extend this with a regex for better validation)
        return email != null && email.contains("@");
    }
}