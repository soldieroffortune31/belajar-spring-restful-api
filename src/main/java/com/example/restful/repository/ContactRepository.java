package com.example.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.restful.entity.Contact;
import com.example.restful.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>, JpaSpecificationExecutor<Contact> {

    Optional<Contact> findFirstByUserAndId(User user, Integer id);

}
