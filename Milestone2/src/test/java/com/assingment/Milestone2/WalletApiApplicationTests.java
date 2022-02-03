package com.assingment.Milestone2;

import com.assingment.Milestone2.helper.JSONReaderHelper;
import com.assingment.Milestone2.service.WalletService;
import com.example.walletapi.dto.JwtReq;
import com.example.walletapi.dto.ResponseObject;
import com.example.walletapi.dto.TransferDetails;
import com.example.walletapi.dto.WalletResponse;
import com.example.walletapi.helper.JSONReaderHelper;
import com.example.walletapi.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WalletApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    /* This method will test the /create-user endpoint. */
    /* PARAMS: JSON type User object */
    @Test
    @Order(1)
    void createUserTestSuccess() throws Exception {

        // Create user here
        String userReq = "src/test/resources/UserReq.json";
//        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        String requestJSON = JSONReaderHelper.read(userReq);

        // Create response object. The response object must be same to what is returned by the controller endpoint.
        String userRes = "src/test/resources/UserRes.json";
        String responseJSON = JSONReaderHelper.read(userRes);
//        String responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));

        /* To create user for the first time. It should send a created response, along with a json
        holding some primary attributed of user.
         */
        mockMvc.perform(MockMvcRequestBuilders.post("/create-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(responseJSON));
    }

    /* Testing if bad request is returned on posting a request to create a user which is already in the database. */
    @Test
    @Order(2)
    void createUserTestFailure() throws Exception {

        // Get the user that is already added in the first test in the database.
        String userReq = "src/test/resources/UserReq.json";
        String requestJSON = JSONReaderHelper.read(userReq);
//        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));

        // Create response object. The response object must be same to what is returned by the controller endpoint.
        String userRes = "src/test/resources/UserResForFailure.json";
        String responseJSON = JSONReaderHelper.read(userRes);
//        String responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));

        /* To check if the same user can be created for the second time.
        The expected response should be bad request. And the response entity will be UserCreationResponse.
         */
        mockMvc.perform(MockMvcRequestBuilders.post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(responseJSON));
    }

    /* Tests whether sending wrong email format returns an error or not. */
    @Test
    @Order(3)
    void createUserTestEmail() throws Exception {
        // Create a user with wrong type of email address.
        String userReq = "src/test/resources/UserReqWithBadEmail.json";
        String requestJSON = JSONReaderHelper.read(userReq);
//        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));

        // Create response object. The response object must be same to what is returned by the controller endpoint.
        String userRes = "src/test/resources/UserResForBadEmail.json";
        String responseJSON = JSONReaderHelper.read(userRes);
//        String responseJSON = new String(Files.readAllBytes(Paths.get(userRes)));

        /* To check if the same user can be created for the second time.
        The expected response should be bad request. And the response entity will be UserCreationResponse.
         */
        mockMvc.perform(MockMvcRequestBuilders.post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(responseJSON));
    }

    /* Login check */
    @Test
    @Order(4)
    void login() throws Exception {
        /* Login with a user already created. */
        JwtReq user = new JwtReq("9044710277", "123456");
        /* The Response will contain the token. */

        String requestJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        /* Check if the token is returned, and is not null. */
        assertNotNull(response);


    }

    /* To test the create-wallet endpoint. This endpoint will function like /create-wallet/{phone_number} */
    @Test
    @Order(5)
    void createWalletTest() throws Exception {
        /* The phone number of the user we are going to create wallet for. */
        String phoneNumber = "9044710277";

        /* Get the token after logging in */
        JwtReq user = new JwtReq(phoneNumber, "123456");
        /* The Response will contain the token. */

        String requestJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        /* Check if the token is returned, and is not null. */
        assertNotNull(response);

        /* Assign token to the static variable of this class. This will enable us to inject
        ever request with the token.
         */
        JSONObject jObject = new JSONObject(response);
        String token = jObject.getString("token");

//        System.out.println("token1 " +token);

        /* Response Object. */
        ResponseObject walletCreationResponse = new ResponseObject("Success", "", new WalletResponse(phoneNumber, "0", true));

        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/" + phoneNumber)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(walletCreationResponse)));

    }

    @Test
    @Order(6)
    void addMoneyToWalletTest() throws Exception {
        String phoneNumber = "9044710277";
        JwtReq user = new JwtReq(phoneNumber, "123456");
        String requestJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        JSONObject jObject = new JSONObject(response);
        String token = jObject.getString("token");

        ResponseObject res = new ResponseObject("Success", "", new WalletResponse(phoneNumber, "100.0", true));

        mockMvc.perform(MockMvcRequestBuilders.put("/add-money/" + phoneNumber + "/" + 100.0f)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(res)));

    }

    @Test
    @Order(7)
    void transferMoneyTest() throws Exception {

        String phoneNumber = "9044710277";
        JwtReq user = new JwtReq(phoneNumber, "123456");
        String requestJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        JSONObject jObject = new JSONObject(response);
        String token = jObject.getString("token");

        TransferDetails det = new TransferDetails();
        det.setPayerPhNo("9044710277");
        det.setPayeePhNo("9044710");
        det.setAmount(50);

        String req = objectMapper.writeValueAsString(det);

        ResponseObject res = new ResponseObject("Failed", "User does not exist", new WalletResponse(phoneNumber, null, false));
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(res)));

    }


}
