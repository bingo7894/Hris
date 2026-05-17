package com.system.hris.auth.service;

public interface EmailService {

    void sendVerificationEmail(String toEmail,String token);

    void sendPasswordResetEmail(String toEmail,String token);

}
