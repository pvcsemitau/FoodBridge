package com.foodbridge;

import com.foodbridge.model.AppUser;
import com.foodbridge.model.Role;
import com.foodbridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FoodBridgeApplication {
    public static void main(String[] args) { SpringApplication.run(FoodBridgeApplication.class, args); }

    @Bean BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean CommandLineRunner seedAdmin(UserRepository users, BCryptPasswordEncoder encoder,
        @Value("${FOODBRIDGE_ADMIN_EMAIL:admin@foodbridge.local}") String email,
        @Value("${FOODBRIDGE_ADMIN_PASSWORD:ChangeMe123!}") String password) {
        return args -> {
            if (users.findByEmailIgnoreCase(email).isEmpty()) {
                AppUser admin = new AppUser();
                admin.setOrganizationName("FoodBridge Admin");
                admin.setLocation("Platform");
                admin.setEmail(email.toLowerCase());
                admin.setPasswordHash(encoder.encode(password));
                admin.setRole(Role.ADMIN);
                admin.setVerified(true);
                users.save(admin);
            }
        };
    }
}
