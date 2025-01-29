package com.example.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restful.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
