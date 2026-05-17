package com.system.hris.auth.service;

import com.system.hris.auth.dto.request.UserRequest;
import com.system.hris.auth.dto.response.EmailValidationResponse;
import com.system.hris.auth.dto.response.LoginResponse;
import com.system.hris.share.dto.response.MessageResponse;
import jakarta.validation.Valid;


public interface AuthService {
    MessageResponse signup(@Valid UserRequest userRequest);

    LoginResponse login(String email, String password);

    EmailValidationResponse validateEmail(String email);

    MessageResponse verifyEmail(String token);

    MessageResponse resendVerificationEmail(String email);

    MessageResponse forgotPassword(String email);

    MessageResponse resetPassword(String token,String newPassword);

    MessageResponse changePassword(String email,  String currentPassword,  String newPassword);

    LoginResponse currentUser(String email);
}
