package com.example.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restful.entity.User;
import com.example.restful.model.ContactReponse;
import com.example.restful.model.CreateContactRequest;
import com.example.restful.model.PagingResponse;
import com.example.restful.model.SearchContactRequest;
import com.example.restful.model.UpdateContactRequest;
import com.example.restful.model.WebResponse;
import com.example.restful.service.ContactService;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
        path = "/api/contacts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactReponse> create(User user, @RequestBody CreateContactRequest request){
        ContactReponse contactReponse = contactService.create(user, request);
        return WebResponse.<ContactReponse>builder().data(contactReponse).build();
    }

    @GetMapping(
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactReponse> get(User user, @PathVariable("contactId") Integer contactId){
        ContactReponse contactReponse = contactService.get(user, contactId);
        return WebResponse.<ContactReponse>builder().data(contactReponse).build();
    }

    @PutMapping(
        path = "/api/contacts/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactReponse> update(User user, @RequestBody UpdateContactRequest request, @PathVariable("contactId") Integer contactId){
        
        request.setId(contactId);
        
        ContactReponse contactReponse = contactService.update(user, request);
        return WebResponse.<ContactReponse>builder().data(contactReponse).build();
    }

    @DeleteMapping(
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("contactId") Integer contactId){
        contactService.delete(user, contactId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
        path = "/api/contacts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactReponse>> search(
        User user, 
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "email", required = false) String email, 
        @RequestParam(value = "phone", required = false) String phone, 
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page, 
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ){
        SearchContactRequest request = SearchContactRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .email(email)
                .phone(phone)
                .build();
        
        Page<ContactReponse> contactReponse = contactService.search(user, request);
        return WebResponse.<List<ContactReponse>>builder()
                .data(contactReponse.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactReponse.getNumber())
                        .totalPage(contactReponse.getTotalPages())
                        .size(contactReponse.getSize())
                        .build())
                .build();
    }

}
