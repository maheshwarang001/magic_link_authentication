package com.example.magic_link_authentication.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;


/** CLASS TO SEND CUSTOM EMAIL TO THE USER **/
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String link){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Verify your email address");
        mailMessage.setText("Please click below");
        mailMessage.setText(link);
        mailMessage.setSentDate(new Date());

        mailSender.send(mailMessage);
    }


}
