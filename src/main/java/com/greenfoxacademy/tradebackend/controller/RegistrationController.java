package com.greenfoxacademy.tradebackend.controller;


import com.greenfoxacademy.tradebackend.exception.exception.UserException;
import com.greenfoxacademy.tradebackend.model.register.ConfirmationResponseDTO;
import com.greenfoxacademy.tradebackend.model.register.RegisterResponseDTO;
import com.greenfoxacademy.tradebackend.model.register.RegistrationRequestDTO;
import com.greenfoxacademy.tradebackend.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegistrationController {


  private final RegistrationService registrationService;

  @Autowired
  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDTO> registerUser(
      @RequestBody(required = false) RegistrationRequestDTO request) throws UserException {
    return ResponseEntity.ok(registrationService.register(request));
  }


  @GetMapping("/register/confirm")
  public ResponseEntity<ConfirmationResponseDTO> validateUser(
      @RequestParam("token") String token) {

    return ResponseEntity.ok(registrationService.confirmToken(token));
  }

}
