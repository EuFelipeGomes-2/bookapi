package com.eufelipegomes.bookapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eufelipegomes.bookapi.dtos.UpdateUserDto;
import com.eufelipegomes.bookapi.exceptions.CustomException;
import com.eufelipegomes.bookapi.models.UserModel;
import com.eufelipegomes.bookapi.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return a empty list of users")
    void testGetAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("Should return a user with the uuid expected")
    void testGetUserById() {
         UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        
        // Mock the save method
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        // Mock the findById method
        when(userRepository.findById(userModel.getUserid())).thenReturn(Optional.of(userModel));

        // Create user
        UserModel createdUser = userService.createUser(userModel);

        UUID userUid = createdUser.getUserid();

        // Get user by ID
        Optional<UserModel> userOptional = userService.getUserById(userUid);
        UserModel user = userOptional.get();
        
        // Validate the UUID
        assertEquals(user.getUserid(), createdUser.getUserid());

    }

    @Test
    @DisplayName("Should return a user created.")
    void testCreateUser() {
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");

        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserModel createdUser = userService.createUser(userModel);

        assertEquals(userModel, createdUser);
    }

    @Test
    @DisplayName("Should delete a user by the given UUID")
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "0felipegomessilva@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        // Mock the findById method to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));

        // Call the deleteUser method
        userService.deleteUser(userId);

        // Verify that the delete method was called with the correct user
        verify(userRepository).delete(userModel);

        // Verify that the findById method was called
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw CustomException when user to delete is not found")
    void testDeleteUserNotFound() {
        UUID userId = UUID.randomUUID();

        // Mock the findById method to return empty
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Assert that CustomException is thrown
        CustomException thrown = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
            userService.deleteUser(userId);
        });

        // Verify the exception message
        org.junit.jupiter.api.Assertions.assertEquals("User Not Found with the id: " + userId, thrown.getMessage());
    }

    @Test
    @DisplayName("Should update user info successfully")
    void testUpdateUserInfoSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel userModel = new UserModel("Felipe", "felipe@gmail.com", "felipe1234");
        userModel.setUserid(userId);

        UpdateUserDto newUserInfo = new UpdateUserDto("Felipe Updated", "felipe.updated@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserModel updatedUser = userService.updateUserInfo(newUserInfo, userId);

        assertEquals(newUserInfo.username(), updatedUser.getUsername());
        assertEquals(newUserInfo.useremail(), updatedUser.getUseremail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(userModel);
    }

    @Test
    @DisplayName("Should throw CustomException when user is not found")
    void testUpdateUserInfoUserNotFound() {
        UUID userId = UUID.randomUUID();
        UpdateUserDto newUserInfo = new UpdateUserDto("Felipe Updated", "felipe.updated@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.updateUserInfo(newUserInfo, userId);
        });

        assertEquals("User Not Found with the id: " + userId, exception.getMessage());

        verify(userRepository).findById(userId);
    }
}
