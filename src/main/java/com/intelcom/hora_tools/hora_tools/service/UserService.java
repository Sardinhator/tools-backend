package com.intelcom.hora_tools.hora_tools.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelcom.hora_tools.hora_tools.dto.UserDTO;
import com.intelcom.hora_tools.hora_tools.entity.User;
import com.intelcom.hora_tools.hora_tools.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

        private final UserRepository userRepository;

        public Page<UserDTO> getUsersWithRolesAndGroups(Pageable pageable, String search) {
                log.debug("Fetching users with roles and groups - Page: {}, Size: {}, Search: {}",
                                pageable.getPageNumber(), pageable.getPageSize(), search);

                Page<User> users;
                if (search != null && !search.trim().isEmpty()) {
                        users = userRepository.searchUsersWithRolesAndGroups(search.trim(), pageable);
                        log.debug("Search returned {} users", users.getTotalElements());
                } else {
                        users = userRepository.findAllWithRolesAndGroups(pageable);
                }

                return users.map(this::convertToDTO);
        }

        private UserDTO convertToDTO(User user) {
                return UserDTO.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .enabled(user.getEnabled())
                                .language(user.getLanguage())
                                .telephone(user.getTelephone())
                                .extension(user.getExtension())
                                .isSystemAdmin(user.getIsSystemAdmin())
                                .isAutomatedTestUser(user.getIsAutomatedTestUser())
                                .locked(user.getLocked())
                                .temporaryPassword(user.getTemporaryPassword())
                                .userType(user.getUserType())
                                .roles(user.getUserRoles() != null ? user.getUserRoles().stream()
                                                .map(ur -> ur.getRole())
                                                .collect(Collectors.toSet()) : null)
                                .groups(user.getUserGroups() != null ? user.getUserGroups().stream()
                                                .map(ug -> ug.getGroupId())
                                                .collect(Collectors.toSet()) : null)
                                .build();
        }
}
