package com.greenfoxacademy.tradebackend.services;

import com.greenfoxacademy.tradebackend.exception.exception.NoSuchUserException;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.repository.UserRepository;
import com.greenfoxacademy.tradebackend.security.Jwt.JwtUtil;
import com.greenfoxacademy.tradebackend.security.Jwt.MyUserDetailsService;
import com.greenfoxacademy.tradebackend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

  private UserService userService;
  private UserRepository mockUserRepository;
//  AuthenticationManager mockAuthenticationManager;
//  MyUserDetailsService userDetailsService;
  private JwtUtil jwtTokenUtil;

  @Before
  public void setUp() {
    mockUserRepository = Mockito.mock(UserRepository.class);
//    mockAuthenticationManager = Mockito.mock(AuthenticationManager.class);
//    userDetailsService = Mockito.mock(MyUserDetailsService.class);
    jwtTokenUtil = Mockito.mock(JwtUtil.class);
    userService =
        new UserService(mockUserRepository,
           jwtTokenUtil);
  }

  @Test
  public void getUserByToken() throws NoSuchUserException {
    String mockToken = "asd.asd.asd";
    String mockUsername = "DummyUser1";
    User mockUser = new User(mockUsername);

    when(jwtTokenUtil.extractUsername(mockToken)).thenReturn(mockUsername);
    when(mockUserRepository.findUserByUsername(mockUsername)).thenReturn(Optional.of(mockUser));

    assertEquals(mockUsername, userService.getUserByToken(mockToken).getUsername());
  }
}
