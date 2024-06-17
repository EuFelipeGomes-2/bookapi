package com.eufelipegomes.bookapi.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eufelipegomes.bookapi.dtos.UpdateUserDto;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.UserRepository;

@Service
public class UserService {

  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserModel> getAllUsers() {
    try {
      List<UserModel> users = userRepository.findAll();

      return users;
    } catch (Exception e) {
      throw new CustomException("An error occurred while fetching users. Plese, try again.");
    }
  }

  public Optional<UserModel> getUserById(UUID userId) {
    Optional<UserModel> user = userRepository.findById(userId);

    if (user.isEmpty()) {
      throw new CustomException("User Not Found with the id: " + userId);
    } else {
      return user;
    }

  }

  public UserModel createUser(UserModel userModel) {
    try {
      String passwordEncoded = new BCryptPasswordEncoder().encode(userModel.getPassword());

      UserModel newUser = new UserModel(userModel.getUsername(), userModel.getUseremail(), passwordEncoded);

      UserModel user = userRepository.save(newUser);

      return user;
    } catch (CustomException e) {
      throw new CustomException("An error ocurred while the user is created.");
    }
  }

  public UserModel updateUserInfo(UpdateUserDto newUserInfo, UUID userId) throws Exception {
    try {
      Optional<UserModel> userOptional = userRepository.findById(userId);

      if (userOptional.isEmpty()) {
        throw new CustomException("User Not Found with the id: " + userId);
      } else {
        UserModel user = userOptional.get();

        if (newUserInfo.username() != null) {
          user.setUsername(newUserInfo.username());
        }
        if (newUserInfo.useremail() != null) {
          user.setUseremail(newUserInfo.useremail());
        }

        return userRepository.save(user);
      }

    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public void deleteUser(UUID userId) throws Exception {
    try {
      Optional<UserModel> userOptional = userRepository.findById(userId);

      if (userOptional.isEmpty()) {
        throw new CustomException("User Not Found with the id: " + userId);
      } else {
        UserModel user = userOptional.get();

        userRepository.delete(user);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }

  }

  public Boolean verifyUserById(UUID userid) throws Exception {
    try {
      Boolean userExists = userRepository.existsById(userid);

      return userExists;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
