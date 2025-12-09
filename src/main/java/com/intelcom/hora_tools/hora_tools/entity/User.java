package com.intelcom.hora_tools.hora_tools.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`users`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "userRoles", "userGroups" })
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "enabled", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean enabled;

    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @Column(name = "extension", length = 30)
    private String extension;

    @Column(name = "is_system_admin", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer isSystemAdmin;

    @Column(name = "is_automated_test_user", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isAutomatedTestUser;

    @Column(name = "locked", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean locked;

    @Column(name = "temporary_password", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean temporaryPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private UserType userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserGroup> userGroups;
}
