package com.intelcom.hora_tools.hora_tools.dto;

import java.util.Set;

import com.intelcom.hora_tools.hora_tools.entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private String language;
    private String telephone;
    private String extension;
    private Integer isSystemAdmin;
    private Boolean isAutomatedTestUser;
    private Boolean locked;
    private Boolean temporaryPassword;
    private UserType userType;
    private Set<String> roles;
    private Set<String> groups;
}
