package com.eufelipegomes.bookapi.dtos;

import com.eufelipegomes.bookapi.enums.UserRole;

public record CreateUserDto(String username, String password, String useremail) {

}
