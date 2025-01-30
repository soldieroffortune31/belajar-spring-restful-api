package com.example.restful.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    void getContactNotFound() throws JsonProcessingException, Exception {
        
        mockMvc.perform(
            get("/api/contacts/78778787")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });

    }

    @Test
    void getContactSuccess() throws JsonProcessingException, Exception {
        
        User user = userRepository.findById("test").orElse(null);
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFirstName("Aliefsufi Uzan");
        contact.setLastName("Kafil Ardi");
        contact.setEmail("uzan310596@gmail.com");
        contact.setPhone("085231728421");

        contactRepository.save(contact);

        mockMvc.perform(
            get("/api/contacts/" + contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<ContactReponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactReponse>>() {
            });

            assertNull(response.getErrors());
            assertEquals(contact.getId(), response.getData().getId());
            assertEquals(contact.getFirstName(), response.getData().getFirstName());
            assertEquals(contact.getLastName(), response.getData().getLastName());
            assertEquals(contact.getEmail(), response.getData().getEmail());
            assertEquals(contact.getPhone(), response.getData().getPhone());
        });

    }

    @Test
    void testUpdateContactBadRequest() throws JsonProcessingException, Exception {
        
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
            put("/api/contacts/1234")
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
    void testUpdateContactSuccess() throws JsonProcessingException, Exception {
        User user = userRepository.findById("test").orElse(null);
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFirstName("Aliefsufi Uzan");
        contact.setLastName("Kafil Ardi");
        contact.setEmail("uzan310596@gmail.com");
        contact.setPhone("085231728421");
        contactRepository.save(contact);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aliefsufi");
        request.setLastName("Uzan");
        request.setEmail("uzan310596@gmail.com");
        request.setPhone("085231728421");


        mockMvc.perform(
            put("/api/contacts/" + contact.getId())
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
            assertEquals(request.getFirstName(), response.getData().getFirstName());
            assertEquals(request.getLastName(), response.getData().getLastName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getPhone(), response.getData().getPhone());

            List<Contact> contacts = contactRepository.findAll();
            assertEquals(1, contacts.size());

        });

    }

    @Test
    void deleteContactNotFound() throws JsonProcessingException, Exception {
        
        mockMvc.perform(
            delete("/api/contacts/78778787")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });

    }

    @Test
    void deleteContactSuccess() throws JsonProcessingException, Exception {
        
        User user = userRepository.findById("test").orElse(null);
        assertNotNull(user);

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFirstName("Aliefsufi Uzan");
        contact.setLastName("Kafil Ardi");
        contact.setEmail("uzan310596@gmail.com");
        contact.setPhone("085231728421");

        contactRepository.save(contact);

        mockMvc.perform(
            delete("/api/contacts/" + contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
        });

    }
}
