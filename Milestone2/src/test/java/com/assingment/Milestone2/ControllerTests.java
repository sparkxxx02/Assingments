package com.assingment.Milestone2;

import com.assingment.Milestone2.controller.Controller;
import com.assingment.Milestone2.helper.JSONReaderHelper;
import com.assingment.Milestone2.model.JwtRequest;
import com.assingment.Milestone2.model.Response;
import com.assingment.Milestone2.model.Wallet;
import com.assingment.Milestone2.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.Transaction;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void login() throws Exception {
        /* Login with a user already created. */
        JwtRequest user = new JwtRequest("admin", "password");
        /* The Response will contain the token. */

        String requestJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        /* Check if the token is returned, and is not null. */
        assertNotNull(response);


    }

    @Test
    void createWalletTest() throws Exception {
        //adding 1st walet
        String token=doLogin();

        String userReq = "src/test/resources/CreateWallet.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
       MvcResult result2 = mockMvc.perform(post("/create_wallet")
               .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
               .content(requestJSON)
               .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isAccepted()).andReturn();

        assertNotNull(result2);

        //adding 2nd wallet
        Wallet wallet2 = new Wallet();
        wallet2.setAmount("100");
        wallet2.setMobilenumber("8791615604");

        String requestJSON2 = objectMapper.writeValueAsString(wallet2);
        result2 = mockMvc.perform(post("/create_wallet")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(requestJSON2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isAccepted()).andReturn();

        assertNotNull(result2);




    }

    @Test
    void list() throws Exception {
        String token=doLogin();
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/wallet")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        assertNotNull(result2);
    }
    @Test
    void transfer() throws Exception {


        String userReq = "src/test/resources/DoTransaction.json";

        String req = new String(Files.readAllBytes(Paths.get(userReq)));
        System.out.println(req);
        String token=doLogin();

        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post("/dotransaction")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isAccepted()).andReturn();

        assertNotNull(result2);

    }

    //this test case will fail coz it would need transactionID which would generate at the time of runtime
    @Test
    void getTransactionSummary() throws Exception {
        String token=doLogin();


        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/transaction?transactionID="+Controller.t_d)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        assertNotNull(result2);

    }

    @Test
    void get_transactions() throws Exception{
        String token=doLogin();
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/all_transactions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

        assertNotNull(result2);
    }
    public String doLogin() throws Exception {
        String phoneNumber = "username";
        JwtRequest user = new JwtRequest(phoneNumber, "password");
        String requestJSON = objectMapper.writeValueAsString(user);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response);

        JSONObject jObject = new JSONObject(response);
        String token= jObject.getString("jwtToken");
        return token;

    }



}
