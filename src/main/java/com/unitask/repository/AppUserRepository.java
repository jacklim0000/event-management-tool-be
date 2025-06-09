package com.unitask.repository;

import com.unitask.constant.Enum.UserRole;
import com.unitask.entity.User.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT user FROM AppUser user " +
            "WHERE user.id IN :ids ")
    List<AppUser> findAllByIds(@Param("ids") Collection<Long> ids);

    @Query("select a from AppUser a where a.userRole = ?1")
    List<AppUser> findByUserRole(UserRole userRole);

}
