package com.atous.auth.presentation.controller;

import com.atous.auth.application.dto.view.PageView;
import com.atous.auth.application.port.in.AssignRolesToUserUseCase;
import com.atous.auth.application.port.in.SearchUsersUseCase;
import com.atous.auth.application.port.in.UpdateUserStatusUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class AdminUserControllerTest {
    @Autowired private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockitoBean private SearchUsersUseCase search;
    @MockitoBean private UpdateUserStatusUseCase updateStatus;
    @MockitoBean private AssignRolesToUserUseCase assignRoles;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void searchUsersShouldBeReachableAtTheDocumentedPathForAdmins() throws Exception {
        when(search.execute(any())).thenReturn(new PageView<>(java.util.List.of(), 0, 20, 0, 0, true, true));

        mockMvc.perform(get("/api/v1/admin/users")
                        .with(jwt().authorities(() -> "ROLE_ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void searchUsersShouldBeForbiddenForNonAdmins() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                        .with(jwt().authorities(() -> "ROLE_USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void searchUsersShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isUnauthorized());
    }
}
