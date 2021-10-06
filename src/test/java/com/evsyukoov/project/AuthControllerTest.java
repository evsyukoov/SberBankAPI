package com.evsyukoov.project;

import com.evsyukoov.project.controller.AuthController;
import com.evsyukoov.project.jwt.JwtUtils;
import com.evsyukoov.project.model.rest.request.AuthRequestParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({TestExtension.class})
public class AuthControllerTest {

    @Autowired
    AuthController controller;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void controllerTest() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void tokenUserTest() throws Exception {
        AuthRequestParams params = new AuthRequestParams();
        params.setName("user");
        params.setPass("1111");
        HttpEntity<AuthRequestParams> request = new HttpEntity<>(params);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(params);

        MvcResult result = mockMvc.perform(post("/service/bank/v1/auth")
                        .contentType("application/json")
                        .content(json))
                .andReturn();

        String token = result.getResponse().getContentAsString();
        String userName = JwtUtils.getUserNameFromJwtToken(token);
        assertThat(userName).isEqualTo("user");
    }

    @Test
    public void tokenAdminTest() throws Exception {
        AuthRequestParams params = new AuthRequestParams();
        params.setName("admin");
        params.setPass("2222");
        HttpEntity<AuthRequestParams> request = new HttpEntity<>(params);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(params);

        MvcResult result = mockMvc.perform(post("/service/bank/v1/auth")
                        .contentType("application/json")
                        .content(json))
                .andReturn();

        String token = result.getResponse().getContentAsString();
        String admin = JwtUtils.getUserNameFromJwtToken(token);
        assertThat(admin).isEqualTo("admin");
    }

    @Test
    public void tokenBadCredentialsTest() throws Exception {
        AuthRequestParams params = new AuthRequestParams();
        params.setName("unknown");
        params.setPass("1111");
        HttpEntity<AuthRequestParams> request = new HttpEntity<>(params);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(params);

        mockMvc.perform(post("/service/bank/v1/auth")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is(401));
    }
}
