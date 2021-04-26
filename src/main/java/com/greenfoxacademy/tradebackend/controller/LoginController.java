package com.greenfoxacademy.tradebackend.controller;


import com.greenfoxacademy.tradebackend.exception.exception.UserException;
import com.greenfoxacademy.tradebackend.model.login.LoginResponseDTO;
import com.greenfoxacademy.tradebackend.security.Jwt.AuthenticationRequest;
import com.greenfoxacademy.tradebackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

  private final LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody(required = false) AuthenticationRequest loginRequest)
      throws UserException {
    return ResponseEntity.ok(loginService.loginUser(loginRequest));
  }

}
