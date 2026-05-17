package com.system.hris.auth.serviceImpl;

import com.system.hris.auth.dao.UserRepository;
import com.system.hris.auth.dto.request.UserRequest;
import com.system.hris.auth.dto.response.EmailValidationResponse;
import com.system.hris.auth.dto.response.LoginResponse;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.auth.entity.User;
import com.system.hris.auth.enums.Role;
import com.system.hris.share.exception.*;
import com.system.hris.share.security.JwtUtil;
import com.system.hris.auth.service.AuthService;
import com.system.hris.auth.service.EmailService;
import com.system.hris.share.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private ServiceUtils serviceUtils;

    @Override
    public MessageResponse signup(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw  new EmailAlreadyExistsException("Email already exists.");
        }

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullname());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setEmailVerified(false);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(userRequest.getEmail(),verificationToken);

        return new MessageResponse("Registration successful! Please check your email to verify your account");
    }

    @Override
    public LoginResponse login(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .filter(u->passwordEncoder.matches(password,u.getPassword()))
                .orElseThrow(()->new BadCredentialsException("invalid email or password"));

        if(!user.isActive()){
            throw new AccountDeactivatedException("Your account has been deactivated Please. contact" +
                    "support for assistance");
        }

        if(!user.isEmailVerified()){
            throw new EmailNotVerifiedException("Please verify your email address before login in. Check your inbox for the verify link.");
        }

        final String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());


        return new LoginResponse(token,user.getEmail(),user.getFullName(),user.getRole().name());
    }

    @Override
    public EmailValidationResponse validateEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        return new EmailValidationResponse(exists,!exists);
    }

    @Override
    public MessageResponse verifyEmail(String token) {
        User user=
                userRepository
                        .findByVerificationToken(token)
                        .orElseThrow(()->new InvalidTokenException("Invalid or expired verification Token"));

        if (user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(Instant.now())) {
            throw  new InvalidTokenException("Verification link has expired. Please request a new one.");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
        return new MessageResponse("Email verified successfully! you can now login.");
    }

    @Override
    public MessageResponse resendVerificationEmail(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(email,verificationToken);

        return  new MessageResponse("Verification email resent Successfully! Please check your inbox.");
    }

    @Override
    public MessageResponse forgotPassword(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email,resetToken);

        return new MessageResponse("Password reset email sent successfully! Please check your inbox.");
    }

    @Override
    public MessageResponse resetPassword(String token,String newPassword) {
        User user =
                userRepository
                        .findByPasswordResetToken(token)
                        .orElseThrow(()->new InvalidTokenException("Invalid or expired reset token"));

        if(user.getPasswordResetTokenExpiry() == null || user.getPasswordResetTokenExpiry().isBefore(Instant.now())){
            throw  new InvalidTokenException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);

        return new MessageResponse("Password reset successfully. You can now login with your new password");

    }

    @Override
    public MessageResponse changePassword(String email, String currentPassword, String newPassword) {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        if(!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password changes successfully");
    }

    @Override
    public LoginResponse currentUser(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        return new LoginResponse(null,user.getEmail(), user.getFullName(),user.getRole().name());
    }


}
