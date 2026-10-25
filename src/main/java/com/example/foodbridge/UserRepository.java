package com.foodbridge.repository;
import com.foodbridge.model.AppUser;
import com.foodbridge.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<AppUser> findByRoleOrderByCreatedAtDesc(Role role);
}
