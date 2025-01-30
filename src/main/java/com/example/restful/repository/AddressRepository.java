package com.example.restful.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restful.entity.Address;
import com.example.restful.entity.Contact;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findFirstByContactAndId(Contact contact, Integer id);

    List<Address> findAllByContact(Contact contact);

}
