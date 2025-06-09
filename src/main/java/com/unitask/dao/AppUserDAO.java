package com.unitask.dao;

import com.unitask.constant.Enum.UserRole;
import com.unitask.constant.error.UserErrorConstant;
import com.unitask.entity.User.AppUser;
import com.unitask.exception.ServiceAppException;
import com.unitask.repository.AppUserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserDAO {

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser save(AppUser appUser) {
        if (Objects.isNull(appUser)) {
            return null;
        }
        return appUserRepository.save(appUser);
    }

    public Optional<AppUser> findOptionalByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new ServiceAppException(HttpStatus.BAD_REQUEST, UserErrorConstant.NOT_FOUND));
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return appUserRepository.findById(id).orElseThrow(() ->
                new ServiceAppException(HttpStatus.BAD_REQUEST, UserErrorConstant.NOT_FOUND));
    }

    public List<AppUser> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return appUserRepository.findAllByIds(ids);
    }

    public List<AppUser> findStudents(){
        return appUserRepository.findByUserRole(UserRole.STUDENT);
    }
}
