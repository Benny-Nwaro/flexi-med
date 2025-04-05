package com.example.flexiMed.notifications.sms;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.exception.AuthenticationException;
import com.twilio.exception.InvalidRequestException;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for sending SMS messages using the Twilio API.
 * This class handles Twilio initialization and provides a method to send SMS messages.
 */
@Service
public class SMSService {

    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

    /**
     * Twilio Account SID, injected from application properties.
     */
    @Value("${twilio.account.sid}")
    private String accountSid;

    /**
     * Twilio Auth Token, injected from application properties.
     */
    @Value("${twilio.auth.token}")
    private String authToken;

    /**
     * Twilio phone number used to send SMS, injected from application properties.
     */
    @Value("${twilio.phone.number}")
    private String twilioNumber;

    /**
     * Initializes the Twilio client after all properties have been injected.
     * This method is called by Spring after dependency injection is complete.
     */
    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        logger.info("Twilio initialized.");
    }

    /**
     * Sends an SMS message to the specified phone number.
     *
     * @param to      The recipient's phone number (in E.164 format).
     * @param message The message body to send.
     * @throws RuntimeException      If an authentication error occurs with Twilio.
     * @throws IllegalArgumentException If an invalid phone number or message format is provided.
     * @throws RuntimeException      If an API error occurs with Twilio.
     * @throws RuntimeException      If any unexpected error occurs during SMS sending.
     */
    public void sendSms(String to, String message) {
        try {
            // Send the SMS
            Message sms = Message.creator(
                    new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(twilioNumber),
                    message
            ).create();
            logger.info("SMS sent successfully to {}: {}", to, message);
        } catch (AuthenticationException e) {
            // Handle authentication errors (invalid SID or token)
            logger.error("Authentication error with Twilio - Invalid SID or token: {}", e.getMessage());
            throw new RuntimeException("Authentication error with Twilio. Please check your credentials.", e);
        } catch (InvalidRequestException e) {
            // Handle invalid arguments (e.g., wrong phone number format)
            logger.error("Invalid argument error while sending SMS: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid phone number or message format.", e);
        } catch (ApiException e) {
            // Handle API errors (e.g., quota exceeded, service down)
            logger.error("API error sending SMS to {}: {}", to, e.getMessage());
            throw new RuntimeException("API error with Twilio while sending SMS. Please try again later.", e);
        } catch (Exception e) {
            // Catch any unexpected errors
            logger.error("Unexpected error while sending SMS to {}: {}", to, e.getMessage());
            throw new RuntimeException("Unexpected error sending SMS. Please contact support.", e);
        }
    }
}