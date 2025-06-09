package com.unitask.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.unitask.constant.Enum.UserRole;
import com.unitask.constant.error.AuthErrorConstant;
import com.unitask.constant.error.UserErrorConstant;
import com.unitask.dao.AppUserDAO;
import com.unitask.dto.DropdownResponse;
import com.unitask.dto.user.ProfileRequest;
import com.unitask.dto.user.ProfileResponse;
import com.unitask.dto.user.ResetPasswordRequest;
import com.unitask.entity.User.AppUser;
import com.unitask.exception.ServiceAppException;
import com.unitask.mapper.UserMapper;
import com.unitask.service.ContextService;
import com.unitask.service.UserService;
import com.unitask.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ContextService implements UserService {

    @Autowired
    private AppUserDAO appUserDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtil;

    private static final int OTP_EXPIRATION_MINUTES = 5;

    private Cache<String, String> otpCache = CacheBuilder.newBuilder().expireAfterWrite(OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES).build();

    @Override
    public void addUser(String username, String password, String name, UserRole userRole) {
        if (appUserDAO.findOptionalByEmail(username).isPresent()) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, UserErrorConstant.EXISTS);
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(username);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setUserRole(userRole);
        appUserDAO.save(appUser);
    }

    @Override
    public ProfileResponse readMyProfile() {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        return userMapper.toResponse(appUser);
    }

    @Override
    public ProfileResponse updateMyProfile(ProfileRequest profileRequest) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        appUser.setName(profileRequest.getName());
        return userMapper.toResponse(appUserDAO.save(appUser));
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        AppUser appUser = appUserDAO.findByEmail(getCurrentAuthUsername());
        if (passwordEncoder.matches(request.getOldPassword(), appUser.getPassword())) {
            appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            appUserDAO.save(appUser);
        } else {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, AuthErrorConstant.INVALID_PASSWORD);
        }
    }

    @Override
    public List<DropdownResponse> getDropdown() {
        return userMapper.toDropdown(appUserDAO.findAll());
    }

    @Override
    public void getOtp(String email) throws MessagingException {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpCache.put(email, otp);
        HashMap<String, Object> values = new HashMap<>();
        values.put("otp", otp);
        emailUtil.sendEmail("OtpTemplate", email, "OTP", values);
    }

    @Override
    public Boolean validateOtp(String email, String otp) {
        String cachedOtp = otpCache.getIfPresent(email);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            otpCache.invalidate(email);
            return true;
        }
        return false;
    }
}
