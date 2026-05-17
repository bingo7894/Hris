package com.system.hris.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message="Current Password required")
    private String currentPassword;

    @NotBlank(message="New Password required")
    private  String newPassword;

}
