package com.example.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.restful.entity.Contact;
import com.example.restful.entity.User;
import com.example.restful.model.ContactReponse;
import com.example.restful.model.CreateContactRequest;
import com.example.restful.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    private ContactReponse toContactReponse(Contact contact){
        return ContactReponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional
    public ContactReponse create(User user, CreateContactRequest request){

        validationService.validate(request);

        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        
        contactRepository.save(contact);

        return toContactReponse(contact);

    }

    @Transactional(readOnly = true)
    public ContactReponse get(User user, Integer id){

        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactReponse(contact);
    }

}
