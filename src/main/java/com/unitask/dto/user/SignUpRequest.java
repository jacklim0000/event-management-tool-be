package com.unitask.dto.user;

import com.unitask.constant.Enum.UserRole;
import lombok.Data;

@Data
public class SignUpRequest {

    private String username;

    private String name;

    private String password;

    private UserRole userRole;
}
