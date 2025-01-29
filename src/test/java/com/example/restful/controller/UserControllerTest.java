package com.example.restful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restful.entity.User;
import com.example.restful.model.RegisterUserRequest;
import com.example.restful.model.UserResponse;
import com.example.restful.model.WebResponse;
import com.example.restful.repository.UserRepository;
import com.example.restful.security.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws JsonProcessingException, Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("test name");

        mockMvc.perform(
            post("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertEquals("OK", response.getData());

        });
    }

    @Test
    void testRegisterBadRequest() throws JsonProcessingException, Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
            post("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());

        });
    }

    @Test
    void testRegisterDuplicate() throws JsonProcessingException, Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("test");
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("test");

        mockMvc.perform(
            post("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getUserUnauthorized() throws Exception{
        mockMvc.perform(
            get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "Gak ada")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getUserUnauthorizedTokenNotSend() throws Exception{
        mockMvc.perform(
            get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getUserSuccess() throws Exception{

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000000L);
        userRepository.save(user);

        mockMvc.perform(
            get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("test", response.getData().getUsername());
            assertEquals("test", response.getData().getName());

        });
    }

    @Test
    void getUserTokenExpired() throws Exception{

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000000000000L);
        userRepository.save(user);

        mockMvc.perform(
            get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());

        });
    }
}
