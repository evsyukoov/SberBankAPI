package com.evsyukoov.project;

import com.evsyukoov.project.controller.ApiMethods;
import com.evsyukoov.project.model.rest.request.AuthRequestParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({TestExtension.class})
public class ApiMethodsTest {

    @Autowired
    private ApiMethods mainMethodsController;

    @Autowired
    private MockMvc mockMvc;

    private String apiMethodsToken;

    @BeforeEach
    public void getToken() throws Exception {
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
        apiMethodsToken = result.getResponse().getContentAsString();
    }

    private String getExpectedJson(String fileName) throws IOException {
        return String.join("", Files.readAllLines(
                Paths.get(
                        this.getClass().getClassLoader()
                                .getResource(fileName).getPath())));
    }

    @Test
    public void controllerTest() {
        assertThat(mainMethodsController).isNotNull();
    }

    @Test
    public void testUnauthorizedUserShowCards() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showCards"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testShowCardsGoodOneCard() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showCards?accountNumber=11112222333344446666").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("showCards1.json")))
                .andDo(print());
    }

    @Test
    public void testShowCardsGoodSeveralCards() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showCards?accountNumber=11112222333344445559").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("showCards2.json")))
                .andDo(print());
    }

    @Test
    public void testShowCardsNoCard() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showCards?accountNumber=123").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }

    @Test
    public void testUnauthorizedUserGetBalance() throws Exception {
        mockMvc.perform(get("/service/bank/v1/getBalance"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetBalanceGood() throws Exception {
        mockMvc.perform(get("/service/bank/v1/getBalance?cardNumber=4444111122223339").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("getBalance.json")))
                .andDo(print());
    }

    @Test
    public void testGetBalanceNoCard() throws Exception {
        mockMvc.perform(get("/service/bank/v1/getBalance?cardNumber=4444").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().json(getExpectedJson("getBalanceBadRequest.json")))
                .andDo(print());
    }

    @Test
    public void testIncrementBalanceExpired() throws Exception {
        mockMvc.perform(get("/service/bank/v1/incrementBalance?cardNumber=4444111122224445&money=10000.000").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(content().json(getExpectedJson("expiredCard1.json")))
                .andDo(print());
    }

    @Test
    public void testIncrementBalance() throws Exception {
        mockMvc.perform(get("/service/bank/v1/incrementBalance?cardNumber=4444111122223337&money=80.00001").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("incrementBalance.json")))
                .andDo(print());
    }

    // номер карты генерируется рандомно, опираясь на тип, поэтому весь ответ не проверяем
    @Test
    public void createCard() throws Exception {
        mockMvc.perform(get("/service/bank/v1/createCard?accountNumber=11112222333344445555&type=VISA").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(0)))
                .andExpect(jsonPath("$.type", equalTo("VISA")))
                .andExpect(jsonPath("$.expireDate", startsWith("2025")))
                .andExpect(jsonPath("$.number", startsWith("4")))
                .andDo(print());
    }

    @Test
    public void createCardErrorCardType() throws Exception {
        mockMvc.perform(get("/service/bank/v1/createCard?accountNumber=11112222333344445555&type=VISAAAA").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().json(getExpectedJson("createCardErrorCardType.json")))
                .andDo(print());
    }
}
