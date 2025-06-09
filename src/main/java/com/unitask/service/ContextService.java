package com.unitask.service;

import com.unitask.constant.Enum.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextService {

    public String getCurrentAuthUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Boolean isStudent() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(x -> UserRole.STUDENT.name().equals(x.getAuthority()));
    }

    public Boolean isLecturer() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(x -> UserRole.LECTURER.name().equals(x.getAuthority()));
    }
}
