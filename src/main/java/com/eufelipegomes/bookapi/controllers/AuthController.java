package com.eufelipegomes.bookapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eufelipegomes.bookapi.dtos.AuthenticationDto;
import com.eufelipegomes.bookapi.dtos.LoginResponseDto;
import com.eufelipegomes.bookapi.infra.security.TokenService;
import com.eufelipegomes.bookapi.models.UserModel;

@Controller
@RequestMapping("auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody AuthenticationDto data) {

    var usernamepassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

    var auth = this.authenticationManager.authenticate(usernamepassword);

    var token = tokenService.generateToken((UserModel) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDto(token));
  }
}
