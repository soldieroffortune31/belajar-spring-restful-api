package com.example.restful.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restful.entity.Address;
import com.example.restful.entity.Contact;
import com.example.restful.entity.User;
import com.example.restful.model.AddressResponse;
import com.example.restful.model.CreateAddressRequest;
import com.example.restful.model.WebResponse;
import com.example.restful.repository.AddressRepository;
import com.example.restful.repository.ContactRepository;
import com.example.restful.repository.UserRepository;
import com.example.restful.security.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Integer contactId;

    @BeforeEach
    void setUp(){
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setFirstName("Aliefsufi Uzan");
        contact.setLastName("Kafil Ardi");
        contact.setEmail("uzan310596@gmail.com");
        contact.setPhone("085231728421");

        contactRepository.save(contact);

        contactId = contact.getId();
    }

    @Test
    void createAddressBadRequest() throws JsonProcessingException, Exception {

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
            post("/api/contacts/1/addresses")
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
    void createAddressSuccess() throws JsonProcessingException, Exception {

        CreateAddressRequest request = new CreateAddressRequest();
        request.setContactId(contactId);
        request.setStreet("Rondokuning");
        request.setCity("Probolinggo");
        request.setProvince("Jawa Timur");
        request.setCountry("Indonesia");
        request.setPostalCode("67282");

        mockMvc.perform(
            post("/api/contacts/"+contactId+"/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {                
            });

            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());
        });

    }

    @Test
    void getAddressNotFound() throws JsonProcessingException, Exception {
        mockMvc.perform(
            get("/api/contacts/1/addresses/1")
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
    void getAddressSuccess() throws JsonProcessingException, Exception {

        Contact contact = contactRepository.findById(contactId).orElse(null);
        assertNotNull(contact);

        Address address = new Address();
        address.setStreet("Rondokuning");
        address.setCity("Probolinggo");
        address.setProvince("Jawa Timur");
        address.setCountry("Indonesia");
        address.setPostalCode("67282");
        address.setContact(contact);

        addressRepository.save(address);

        mockMvc.perform(
            get("/api/contacts/"+contactId+"/addresses/"+address.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {                
            });

            assertNull(response.getErrors());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });

    }
}
