package com.unitask.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !StringUtils.equalsIgnoreCase(auth.getName(), "anonymousUser")) {
            return Optional.of(auth.getName());
        }
        return Optional.of("DEFAULT_USER");
    }
}
