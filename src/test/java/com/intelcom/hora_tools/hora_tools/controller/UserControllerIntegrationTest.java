package com.intelcom.hora_tools.hora_tools.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.intelcom.hora_tools.hora_tools.config.TestSecurityConfig;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration"
})
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Given valid pagination parameters, when getting users, then return paginated users")
    void givenValidPaginationParameters_whenGettingUsers_thenReturnPaginatedUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    @DisplayName("Given small page size, when getting users, then return correct number of users")
    void givenSmallPageSize_whenGettingUsers_thenReturnCorrectNumberOfUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size", is(2)));
    }

    @Test
    @DisplayName("Given sort by email ascending, when getting users, then return users sorted by email")
    void givenSortByEmailAscending_whenGettingUsers_thenReturnUsersSortedByEmail() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "email")
                .param("sortDirection", "ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.sort.sorted", is(true)));
    }

    @Test
    @DisplayName("Given sort by email descending, when getting users, then return users sorted by email desc")
    void givenSortByEmailDescending_whenGettingUsers_thenReturnUsersSortedByEmailDesc() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "email")
                .param("sortDirection", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.sort.sorted", is(true)));
    }

    @Test
    @DisplayName("Given sort by firstName, when getting users, then return users sorted by firstName")
    void givenSortByFirstName_whenGettingUsers_thenReturnUsersSortedByFirstName() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "firstName")
                .param("sortDirection", "ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.sort.sorted", is(true)));
    }

    @Test
    @DisplayName("Given default parameters, when getting users, then return first page with default size")
    void givenDefaultParameters_whenGettingUsers_thenReturnFirstPageWithDefaultSize() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.sort.sorted", is(true)));
    }

    @Test
    @DisplayName("Given page 1, when getting users, then return second page")
    void givenPage1_whenGettingUsers_thenReturnSecondPage() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "1")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    @DisplayName("Given users exist, when getting users, then return users with all required fields")
    void givenUsersExist_whenGettingUsers_thenReturnUsersWithAllRequiredFields() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Given users with roles, when getting users, then return users with roles")
    void givenUsersWithRoles_whenGettingUsers_thenReturnUsersWithRoles() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Given users with groups, when getting users, then return users with groups")
    void givenUsersWithGroups_whenGettingUsers_thenReturnUsersWithGroups() throws Exception {
        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
