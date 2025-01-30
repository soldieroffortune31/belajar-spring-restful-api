package com.example.restful.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.restful.entity.Contact;
import com.example.restful.entity.User;
import com.example.restful.model.ContactReponse;
import com.example.restful.model.CreateContactRequest;
import com.example.restful.model.SearchContactRequest;
import com.example.restful.model.UpdateContactRequest;
import com.example.restful.repository.ContactRepository;

import jakarta.persistence.criteria.Predicate;

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

    @Transactional
    public ContactReponse update(User user, UpdateContactRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return toContactReponse(contact);
    
    }

    @Transactional
    public void delete(User user, Integer contactId){

        Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        
        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactReponse> search(User user, SearchContactRequest request){

        Specification<Contact> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if(Objects.nonNull(request.getName())){
                predicates.add(builder.or(
                    builder.like(root.get("firstName"), "%" + request.getName() + "%"),
                    builder.like(root.get("lastName"), "%" + request.getName() + "%")
                ));
            }
            if(Objects.nonNull(request.getEmail())){
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }

            if(Objects.nonNull(request.getPhone())){
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactReponse> contactReponses = contacts.getContent().stream()
            .map(contact -> toContactReponse(contact)).collect(Collectors.toList());

        return new PageImpl<>(contactReponses, pageable, contacts.getTotalElements());
    }

}
