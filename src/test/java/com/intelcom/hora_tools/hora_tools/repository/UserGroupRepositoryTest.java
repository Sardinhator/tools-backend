package com.intelcom.hora_tools.hora_tools.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.intelcom.hora_tools.hora_tools.entity.User;
import com.intelcom.hora_tools.hora_tools.entity.UserGroup;
import com.intelcom.hora_tools.hora_tools.entity.UserType;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("UserGroup Repository Tests")
class UserGroupRepositoryTest {

        @Autowired
        private UserGroupRepository userGroupRepository;

        @Autowired
        private UserRepository userRepository;

        private User testUser1;
        private User testUser2;
        private static final String GROUP_ID_1 = "group-1";
        private static final String GROUP_ID_2 = "group-2";

        @BeforeEach
        void setUp() {
                // Clean up before each test
                userGroupRepository.deleteAll();
                userRepository.deleteAll();

                // Create test users
                testUser1 = User.builder()
                                .id("user-1")
                                .email("test1@example.com")
                                .password("password123")
                                .firstName("John")
                                .lastName("Doe")
                                .enabled(true)
                                .isSystemAdmin(0)
                                .isAutomatedTestUser(false)
                                .locked(false)
                                .temporaryPassword(false)
                                .userType(UserType.USER)
                                .build();

                testUser2 = User.builder()
                                .id("user-2")
                                .email("test2@example.com")
                                .password("password456")
                                .firstName("Jane")
                                .lastName("Smith")
                                .enabled(true)
                                .isSystemAdmin(0)
                                .isAutomatedTestUser(false)
                                .locked(false)
                                .temporaryPassword(false)
                                .userType(UserType.USER)
                                .build();

                userRepository.save(testUser1);
                userRepository.save(testUser2);
        }

        @Test
        @DisplayName("Given a valid user group, When saved, Then it should persist successfully")
        void givenValidUserGroup_whenSaved_thenPersistsSuccessfully() {
                // Given
                UserGroup userGroup = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();

                // When
                UserGroup saved = userGroupRepository.save(userGroup);

                // Then
                assertThat(saved).isNotNull();
                assertThat(saved.getUserId()).isEqualTo(testUser1.getId());
                assertThat(saved.getGroupId()).isEqualTo(GROUP_ID_1);
        }

        @Test
        @DisplayName("Given user groups exist, When finding by user ID, Then returns all groups for that user")
        void givenUserGroupsExist_whenFindingByUserId_thenReturnsAllGroupsForUser() {
                // Given
                UserGroup userGroup1 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();
                UserGroup userGroup2 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_2)
                                .build();
                UserGroup userGroup3 = UserGroup.builder()
                                .userId(testUser2.getId())
                                .groupId(GROUP_ID_1)
                                .build();

                userGroupRepository.saveAll(List.of(userGroup1, userGroup2, userGroup3));

                // When
                List<UserGroup> userGroups = userGroupRepository.findByUserId(testUser1.getId());

                // Then
                assertThat(userGroups).hasSize(2);
                assertThat(userGroups).extracting(UserGroup::getGroupId)
                                .containsExactlyInAnyOrder(GROUP_ID_1, GROUP_ID_2);
        }

        @Test
        @DisplayName("Given user groups exist, When finding by group ID, Then returns all users in that group")
        void givenUserGroupsExist_whenFindingByGroupId_thenReturnsAllUsersInGroup() {
                // Given
                UserGroup userGroup1 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();
                UserGroup userGroup2 = UserGroup.builder()
                                .userId(testUser2.getId())
                                .groupId(GROUP_ID_1)
                                .build();

                userGroupRepository.saveAll(List.of(userGroup1, userGroup2));

                // When
                List<UserGroup> userGroups = userGroupRepository.findByGroupId(GROUP_ID_1);

                // Then
                assertThat(userGroups).hasSize(2);
                assertThat(userGroups).extracting(UserGroup::getUserId)
                                .containsExactlyInAnyOrder(testUser1.getId(), testUser2.getId());
        }

        @Test
        @DisplayName("Given user groups exist, When deleting by user ID, Then removes all groups for that user")
        void givenUserGroupsExist_whenDeletingByUserId_thenRemovesAllGroupsForUser() {
                // Given
                UserGroup userGroup1 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();
                UserGroup userGroup2 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_2)
                                .build();
                UserGroup userGroup3 = UserGroup.builder()
                                .userId(testUser2.getId())
                                .groupId(GROUP_ID_1)
                                .build();

                userGroupRepository.saveAll(List.of(userGroup1, userGroup2, userGroup3));

                // When
                userGroupRepository.deleteByUserId(testUser1.getId());
                userGroupRepository.flush();

                // Then
                List<UserGroup> remainingGroups = userGroupRepository.findAll();
                assertThat(remainingGroups).hasSize(1);
                assertThat(remainingGroups.get(0).getUserId()).isEqualTo(testUser2.getId());
        }

        @Test
        @DisplayName("Given user groups exist, When deleting by group ID, Then removes all users from that group")
        void givenUserGroupsExist_whenDeletingByGroupId_thenRemovesAllUsersFromGroup() {
                // Given
                UserGroup userGroup1 = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();
                UserGroup userGroup2 = UserGroup.builder()
                                .userId(testUser2.getId())
                                .groupId(GROUP_ID_1)
                                .build();
                UserGroup userGroup3 = UserGroup.builder()
                                .userId(testUser2.getId())
                                .groupId(GROUP_ID_2)
                                .build();

                userGroupRepository.saveAll(List.of(userGroup1, userGroup2, userGroup3));

                // When
                userGroupRepository.deleteByGroupId(GROUP_ID_1);
                userGroupRepository.flush();

                // Then
                List<UserGroup> remainingGroups = userGroupRepository.findAll();
                assertThat(remainingGroups).hasSize(1);
                assertThat(remainingGroups.get(0).getGroupId()).isEqualTo(GROUP_ID_2);
        }

        @Test
        @DisplayName("Given a user group exists, When checking existence, Then returns correct boolean value")
        void givenUserGroupExists_whenCheckingExistence_thenReturnsCorrectBooleanValue() {
                // Given
                UserGroup userGroup = UserGroup.builder()
                                .userId(testUser1.getId())
                                .groupId(GROUP_ID_1)
                                .build();

                userGroupRepository.save(userGroup);

                // When & Then
                assertThat(userGroupRepository.existsByUserIdAndGroupId(testUser1.getId(), GROUP_ID_1))
                                .isTrue();
                assertThat(userGroupRepository.existsByUserIdAndGroupId(testUser1.getId(), GROUP_ID_2))
                                .isFalse();
                assertThat(userGroupRepository.existsByUserIdAndGroupId(testUser2.getId(), GROUP_ID_1))
                                .isFalse();
        }

        @Test
        @DisplayName("Given no groups for user, When finding by user ID, Then returns empty list")
        void givenNoGroupsForUser_whenFindingByUserId_thenReturnsEmptyList() {
                // When
                List<UserGroup> userGroups = userGroupRepository.findByUserId("non-existent-user");

                // Then
                assertThat(userGroups).isEmpty();
        }

        @Test
        @DisplayName("Given no users in group, When finding by group ID, Then returns empty list")
        void givenNoUsersInGroup_whenFindingByGroupId_thenReturnsEmptyList() {
                // When
                List<UserGroup> userGroups = userGroupRepository.findByGroupId("non-existent-group");

                // Then
                assertThat(userGroups).isEmpty();
        }
}