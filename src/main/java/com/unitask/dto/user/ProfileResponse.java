package com.unitask.dto.user;

import lombok.Data;

@Data
public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
}
