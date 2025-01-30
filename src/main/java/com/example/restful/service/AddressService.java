package com.example.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.restful.entity.Address;
import com.example.restful.entity.Contact;
import com.example.restful.entity.User;
import com.example.restful.model.AddressResponse;
import com.example.restful.model.CreateAddressRequest;
import com.example.restful.repository.AddressRepository;
import com.example.restful.repository.ContactRepository;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    private AddressResponse toAddressResponse(Address address){
        return AddressResponse.builder()
            .id(address.getId())
            .street(address.getStreet())
            .city(address.getCity())
            .province(address.getProvince())
            .country(address.getCountry())
            .postalCode(address.getPostalCode())
            .build();
    }

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);

        return toAddressResponse(address);

    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, Integer contactId, Integer addressId){

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not Found"));

        return toAddressResponse(address);
    }


}
