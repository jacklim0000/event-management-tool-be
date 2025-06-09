package com.unitask.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String jwt;
    private String userRole;
    private String name;
    private String email;

}
