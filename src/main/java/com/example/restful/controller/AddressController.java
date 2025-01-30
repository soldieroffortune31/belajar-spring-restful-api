package com.example.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restful.entity.User;
import com.example.restful.model.AddressResponse;
import com.example.restful.model.CreateAddressRequest;
import com.example.restful.model.UpdateAddressRequest;
import com.example.restful.model.WebResponse;
import com.example.restful.service.AddressService;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
        path = "/api/contacts/{contactId}/addresses",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(
        User user, 
        @RequestBody CreateAddressRequest request, 
        @PathVariable("contactId") Integer contactId
    ){

        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(
        User user,
        @PathVariable("contactId") Integer contactId,
        @PathVariable("addressId") Integer addressId
    ){
        AddressResponse addressResponse = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }
    
    @PutMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(
        User user, 
        @RequestBody UpdateAddressRequest request, 
        @PathVariable("contactId") Integer contactId,
        @PathVariable("addressId") Integer addressId
    ){
        
        request.setContactId(contactId);
        request.setAddressId(addressId);

        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }


    @DeleteMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> remove(
        User user, 
        @PathVariable("contactId") Integer contactId,
        @PathVariable("addressId") Integer addressId
    ){

        addressService.remove(user, contactId, addressId);
        return WebResponse.<String>builder().data("OK").build();
    }

}
