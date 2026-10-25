package com.foodbridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=120) private String organizationName;
    @Column(nullable=false, length=160) private String location;
    @Column(nullable=false, unique=true, length=190) private String email;
    @Column(nullable=false, length=100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Role role;
    @Column(nullable=false) private boolean verified;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId(){return id;} public String getOrganizationName(){return organizationName;}
    public void setOrganizationName(String v){organizationName=v;} public String getLocation(){return location;}
    public void setLocation(String v){location=v;} public String getEmail(){return email;}
    public void setEmail(String v){email=v;} public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String v){passwordHash=v;} public Role getRole(){return role;}
    public void setRole(Role v){role=v;} public boolean isVerified(){return verified;}
    public void setVerified(boolean v){verified=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
