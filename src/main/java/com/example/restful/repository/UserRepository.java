package com.example.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restful.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
