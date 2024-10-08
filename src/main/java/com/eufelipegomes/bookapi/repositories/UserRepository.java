package com.eufelipegomes.bookapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.eufelipegomes.bookapi.models.UserModel;


@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
  UserDetails findByUseremail(String useremail);
}
