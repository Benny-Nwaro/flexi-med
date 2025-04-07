package com.example.flexiMed.notifications.email;

import java.util.Map;

/**
 * Represents the request body for sending emails via the {@link EmailController}.
 * This class encapsulates the necessary information to send an email,
 * including the recipient's address, the email subject, the name of the
 * Thymeleaf template to use for the email body, and any variables required
 * by the template.
 */
public class EmailRequest {
    /**
     * The email address of the recipient.
     */
    private String to;

    /**
     * The subject line of the email.
     */
    private String subject;

    /**
     * The name of the Thymeleaf template (without the .html extension)
     * to be processed and used as the email body.
     */
    private String text; // Template name

    /**
     * A map of key-value pairs representing the variables to be passed
     * to the Thymeleaf template for dynamic content generation.
     */
    private Map<String, Object> contextVariables;

    /**
     * Retrieves the recipient's email address.
     *
     * @return The recipient's email address.
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the recipient's email address.
     *
     * @param to The recipient's email address.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Retrieves the subject line of the email.
     *
     * @return The subject line of the email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject line of the email.
     *
     * @param subject The subject line of the email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Retrieves the name of the Thymeleaf template to be used for the email body.
     *
     * @return The name of the Thymeleaf template.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the name of the Thymeleaf template to be used for the email body.
     *
     * @param text The name of the Thymeleaf template.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Retrieves the map of context variables to be passed to the Thymeleaf template.
     *
     * @return A map of context variables.
     */
    public Map<String, Object> getContextVariables() {
        return contextVariables;
    }

    /**
     * Sets the map of context variables to be passed to the Thymeleaf template.
     *
     * @param contextVariables A map of context variables.
     */
    public void setContextVariables(Map<String, Object> contextVariables) {
        this.contextVariables = contextVariables;
    }
}