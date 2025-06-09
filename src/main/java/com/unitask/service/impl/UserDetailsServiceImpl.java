package com.unitask.service.impl;

import com.unitask.entity.User.AppUser;
import com.unitask.repository.AppUserRepository;
import com.unitask.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("No user with this user name");
        } else {
            System.out.println(user.getEmail());
        }

        return new CustomUserDetails(
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getUserRole().name())));
    }

}