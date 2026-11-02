package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User extends GlobalRecord{
    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 254)
    private String emailAddress;

    @Column(length = 30)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    private boolean emailVerified;
    private boolean phoneVerified;


    @ManyToMany
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserStatus userStatus = UserStatus.PENDING_VERIFICATION;

    private Instant lastLoginAt;
}
