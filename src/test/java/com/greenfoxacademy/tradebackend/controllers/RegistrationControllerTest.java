package com.greenfoxacademy.tradebackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.tradebackend.controller.RegistrationController;
import com.greenfoxacademy.tradebackend.exception.exception.MissingParameterException;
import com.greenfoxacademy.tradebackend.exception.exception.ReservedUsernameException;
import com.greenfoxacademy.tradebackend.model.register.RegistrationRequestDTO;
import com.greenfoxacademy.tradebackend.repository.UserRepository;
import com.greenfoxacademy.tradebackend.security.Jwt.JwtUtil;
import com.greenfoxacademy.tradebackend.security.Jwt.MyUserDetailsService;
import com.greenfoxacademy.tradebackend.service.RegistrationService;
import com.greenfoxacademy.tradebackend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  @MockBean
  UserService mockUserService;
  @Autowired
  @MockBean
  UserRepository mockUserRepository;
  @Autowired
  @MockBean
  RegistrationService mockRegistrationService;
  @Autowired
  @MockBean
  MyUserDetailsService mockMyUserDetailsService;
  @Autowired
  @MockBean
  JwtUtil mockJwtUtil;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  @MockBean
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void registerUser_DuplicateUsername_ReservedUsernameExceptionExpected() throws Exception {
    RegistrationRequestDTO testRegistrationData = new RegistrationRequestDTO("DummyUser", "test@email.com", "1234");

    when(mockRegistrationService.register(any())).thenThrow(new ReservedUsernameException());

    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRegistrationData)))
        .andExpect(status().isConflict())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ReservedUsernameException));
  }

  @Test
  public void registerUser_MissingParameters_MissingParametersExceptionExpected() throws Exception {
    RegistrationRequestDTO testRegistrationData = new RegistrationRequestDTO("DummyUser", "", "");

    when(mockRegistrationService.register(any()))
        .thenThrow(new MissingParameterException(Arrays.asList("password", "e-mail")));

    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testRegistrationData)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingParameterException));
  }
}
