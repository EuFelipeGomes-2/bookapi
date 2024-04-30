package com.eufelipegomes.bookapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eufelipegomes.bookapi.dtos.CreateUserDto;
import com.eufelipegomes.bookapi.dtos.UpdateUserDto;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public ResponseEntity<List<UserModel>> getAllUsers() {
    try {
      List<UserModel> users = userService.getAllUsers();
      return ResponseEntity.status(HttpStatus.OK).body(users);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserModel> getUserById(@PathVariable("id") UUID userId) {
    try {
      Optional<UserModel> user = userService.getUserById(userId);
      return user.map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PostMapping("/")
  public ResponseEntity<UserModel> createUser(@RequestBody CreateUserDto userInfo) {
    try {
      var userModel = new UserModel();
      BeanUtils.copyProperties(userInfo, userModel);
      UserModel createdUser = userService.createUser(userModel);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserModel> updateUserInfo(@RequestBody UpdateUserDto newUserInfo,
      @PathVariable("id") UUID userId) {
    try {
      UserModel updatedUser = userService.updateUserInfo(newUserInfo, userId);

      return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") UUID userId) {
    try {
      userService.deleteUser(userId);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deleted");
    } catch (CustomException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
