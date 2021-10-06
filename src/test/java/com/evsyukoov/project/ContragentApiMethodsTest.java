package com.evsyukoov.project;

import com.evsyukoov.project.controller.ApiMethods;
import com.evsyukoov.project.model.enums.AccountType;
import com.evsyukoov.project.model.enums.Bank;
import com.evsyukoov.project.model.rest.request.AddContragentRequestParams;
import com.evsyukoov.project.model.rest.request.AuthRequestParams;
import com.evsyukoov.project.model.rest.request.TransactionRequestParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({TestExtension.class})
public class ContragentApiMethodsTest {
    @Autowired
    private ApiMethods mainMethodsController;

    @Autowired
    private MockMvc mockMvc;

    private String apiMethodsToken;

    @BeforeEach
    public void getToken() throws Exception {
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
        apiMethodsToken = result.getResponse().getContentAsString();
    }

    private String getExpectedJson(String fileName) throws IOException {
        return String.join("", Files.readAllLines(
                Paths.get(
                        this.getClass().getClassLoader()
                                .getResource(fileName).getPath())));
    }

    @Test
    public void testUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showAllContragents"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testShowCurrentBankContragents() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showAllContragents?bank=ALPHA").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("allContragentsOfCurrentBank.json")))
                .andDo(print());
    }

    @Test
    public void testShowCurrentBankContragentsUnknownParam() throws Exception {
        mockMvc.perform(get("/service/bank/v1/showAllContragents?bank=KEK").
                        header("Authorization", "Bearer " + apiMethodsToken))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().json(getExpectedJson("showCurrentBankContragentsUnknownParam.json")))
                .andDo(print());
    }

    @Test
    public void testAddContragent() throws Exception {
        AddContragentRequestParams body = new AddContragentRequestParams();
        body.setName("Evsukov Denis Konstantinovich");
        body.setAccountNumber("45672222333344445556");
        body.setAccountType(AccountType.MULTI.name());
        body.setBank(Bank.VTB.name());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/service/bank/v1/addContragent").
                        header("Authorization", "Bearer " + apiMethodsToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("addContragent.json")))
                .andDo(print());
    }

    @Test
    public void testAddContragentIfAccountNumberExists() throws Exception {
        AddContragentRequestParams body = new AddContragentRequestParams();
        body.setName("Testovii Test Test");
        body.setAccountNumber("11112222333344445555");
        body.setAccountType(AccountType.MULTI.name());
        body.setBank(Bank.SBERBANK.name());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/service/bank/v1/addContragent").
                        header("Authorization", "Bearer " + apiMethodsToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().json(getExpectedJson("addContragentIfAccountNumberExists.json")))
                .andDo(print());
    }

    @Test
    public void testTransaction() throws Exception {
        TransactionRequestParams body = new TransactionRequestParams();
        body.setCardNumberFrom("6444411122224445");
        body.setCardNumberTo("4444111122224444");
        body.setMoney(new BigDecimal(20));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/service/bank/v1/doTransaction").
                        header("Authorization", "Bearer " + apiMethodsToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(getExpectedJson("transaction.json")))
                .andDo(print());
    }

    @Test
    public void testTransactionExpired() throws Exception {
        TransactionRequestParams body = new TransactionRequestParams();
        body.setCardNumberFrom("6444411122224445");
        body.setCardNumberTo("4444111122224445");
        body.setMoney(new BigDecimal(20));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/service/bank/v1/doTransaction").
                        header("Authorization", "Bearer " + apiMethodsToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().json(getExpectedJson("expiredCard2.json")))
                .andDo(print());
    }

    @Test
    public void testTransactionNoMoney() throws Exception {
        TransactionRequestParams body = new TransactionRequestParams();
        body.setCardNumberFrom("4444111122224444");
        body.setCardNumberTo("4444111122223339");
        body.setMoney(new BigDecimal(20));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/service/bank/v1/doTransaction").
                        header("Authorization", "Bearer " + apiMethodsToken)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().json(getExpectedJson("transactionNoMoney.json")))
                .andDo(print());
    }
}
