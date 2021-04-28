package com.greenfoxacademy.tradebackend.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.model.user.UserRole;
import com.greenfoxacademy.tradebackend.repository.UserRepository;
import com.greenfoxacademy.tradebackend.security.Jwt.AuthenticationRequest;
import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationTokenService;
import com.greenfoxacademy.tradebackend.service.SignUpService;
import com.greenfoxacademy.tradebackend.service.UserService;

import com.greenfoxacademy.tradebackend.service.email.EmailSender;
import com.greenfoxacademy.tradebackend.service.email.EmailService;
import com.greenfoxacademy.tradebackend.service.email.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  SignUpService signUpService;

  @Autowired
  EmailValidator emailValidator;

  @Autowired
  ConfirmationTokenService confirmationTokenService;

  @Autowired
  EmailSender emailSender;

  @Autowired
  EmailService emailService;

  @Test
  public void givenLoginURL_whenMockMVC_thenStatusOK_andReturnsWithUserLoginDTO() throws Exception {
//    String encodedPassword = PasswordSecurity.getInstance().encode("LoginPassword");
    userRepository.save(new User("LoginDummyUser",
            "test@email.com",
        "LoginPassword", UserRole.USER));

    mockMvc.perform(get("/register/confirm")
        .contentType(MediaType.APPLICATION_JSON)
        .param("token", "asd-asd-asd-asd"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("status", is("ok")))
        .andDo(print());

    AuthenticationRequest validUser = new AuthenticationRequest("LoginDummyUser", "LoginPassword");

    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("status", is("ok")))
        .andDo(print());
  }
}
