package com.example.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restful.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
