package com.example.restful.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restful.entity.Contact;
import com.example.restful.entity.User;
import com.example.restful.model.ContactReponse;
import com.example.restful.model.CreateContactRequest;
import com.example.restful.model.WebResponse;
import com.example.restful.repository.ContactRepository;
import com.example.restful.repository.UserRepository;
import com.example.restful.security.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp(){
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);
    }

    @Test
    void testCreateContactBadRequest() throws JsonProcessingException, Exception {
        
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
            post("/api/contacts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });

    }


    @Test
    void testCreateContactSuccess() throws JsonProcessingException, Exception {
        
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aliefsufi Uzan");
        request.setLastName("Kafil Ardi");
        request.setEmail("uzan310596@gmail.com");
        request.setPhone("085231728421");


        mockMvc.perform(
            post("/api/contacts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<ContactReponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactReponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals("Aliefsufi Uzan", response.getData().getFirstName());
            assertEquals("Kafil Ardi", response.getData().getLastName());
            assertEquals("uzan310596@gmail.com", response.getData().getEmail());
            assertEquals("085231728421", response.getData().getPhone());

            List<Contact> contacts = contactRepository.findAll();
            assertEquals(1, contacts.size());

        });

    }
}
