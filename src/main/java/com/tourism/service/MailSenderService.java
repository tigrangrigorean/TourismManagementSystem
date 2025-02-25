package com.tourism.service;


import com.tourism.exceptions.CustomExceptions;
import com.tourism.model.domain.Customer;
import com.tourism.model.dto.ForgotPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    private final CustomerService customerService;

    private final ForgotPassword forgotPassword;

    @Value("$(spring.email.username)")
    private String fromMail;

    public void sendEmail(String mail, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }

    public String handlePinRequest(String email, boolean checkVerifyMail) {

        Customer customer = customerService.findCustomerByEmail(email)
                .orElseThrow(() -> new CustomExceptions.CustomerNotFoundException("Customer not found with email: " + email));

        if (checkVerifyMail && !customer.getVerifyMail()) {
            throw new CustomExceptions.CustomerNotFoundException("Customer is not active. Please verify your email.");
        }

        String pin = forgotPassword.generatePin();
        customer.setPin(pin);
        customerService.update(customer);

        sendEmail(email, "Verify code", pin);

        schedulePinReset(email);

        return "A verification code has been sent to your email.";
    }


    public void schedulePinReset(String email) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            customerService.findCustomerByEmail(email)
                    .ifPresent(customer -> {
                        customer.setPin(null);
                        customerService.update(customer);
                    });
        }, 30, TimeUnit.MINUTES);
    }
}
