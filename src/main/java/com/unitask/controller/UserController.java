package com.unitask.controller;

import com.unitask.dto.DropdownResponse;
import com.unitask.dto.user.ProfileRequest;
import com.unitask.dto.user.ProfileResponse;
import com.unitask.dto.user.ResetPasswordRequest;
import com.unitask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = "user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/dropdown")
    public ResponseEntity<List<DropdownResponse>> dropdown() {
        return ResponseEntity.ok(userService.getDropdown());
    }

    @PutMapping("my-profile")
    public ResponseEntity<ProfileResponse> update(@RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.ok(userService.updateMyProfile(profileRequest));
    }

    @GetMapping("my-profile")
    public ResponseEntity<ProfileResponse> update() {
        return ResponseEntity.ok(userService.readMyProfile());
    }

    @PutMapping("reset-password")
    public ResponseEntity<?> update(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
