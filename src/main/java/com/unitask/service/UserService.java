package com.unitask.service;

import com.unitask.constant.Enum.UserRole;
import com.unitask.dto.DropdownResponse;
import com.unitask.dto.user.ProfileRequest;
import com.unitask.dto.user.ProfileResponse;
import com.unitask.dto.user.ResetPasswordRequest;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {
    void addUser(String username, String password, String name, UserRole userRole);

    ProfileResponse readMyProfile();

    ProfileResponse updateMyProfile(ProfileRequest profileRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    List<DropdownResponse> getDropdown();

    void getOtp(String email) throws MessagingException;

    Boolean validateOtp(String email,String otp);
}
