package com.unitask.dto.user;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
