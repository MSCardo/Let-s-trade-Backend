package com.greenfoxacademy.tradebackend.service;


import com.greenfoxacademy.tradebackend.exception.exception.MissingParameterException;
import com.greenfoxacademy.tradebackend.exception.exception.NotValidatedUserException;
import com.greenfoxacademy.tradebackend.exception.exception.UserException;
import com.greenfoxacademy.tradebackend.model.login.LoginResponseDTO;
import com.greenfoxacademy.tradebackend.security.Jwt.AuthenticationRequest;
import com.greenfoxacademy.tradebackend.security.Jwt.AuthenticationResponse;
import com.greenfoxacademy.tradebackend.security.Jwt.JwtUtil;
import com.greenfoxacademy.tradebackend.security.Jwt.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LoginService {


  private final UserService userService;
  private final JwtUtil jwtTokenUtil;
  private final MyUserDetailsService userDetailsService;
  private final DaoAuthenticationProvider daoAuthenticationProvider;

  @Autowired
  public LoginService(UserService userService, JwtUtil jwtTokenUtil,
                      MyUserDetailsService userDetailsService,
                      DaoAuthenticationProvider daoAuthenticationProvider) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
    this.daoAuthenticationProvider = daoAuthenticationProvider;
  }


  public LoginResponseDTO loginUser(AuthenticationRequest loginRequest) throws UserException {

    if (loginRequest == null || isUsernameMissing(loginRequest) && isPasswordMissing(loginRequest)) {
      throw new MissingParameterException(Arrays.asList("username", "password"));
    }
    checkForMissingLoginParameters(loginRequest);

    isValidated(loginRequest);

    final UserDetails userDetails;
    userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    daoAuthenticationProvider.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));
    final String jwt = jwtTokenUtil.generateToken(userDetails);
    return new LoginResponseDTO(new AuthenticationResponse(jwt));
  }

  private void isValidated(AuthenticationRequest loginRequest) throws NotValidatedUserException {
    if (!userService.loadUserByUsername(loginRequest.getUsername()).isEnabled()){
      throw new NotValidatedUserException();
    }
  }


  public void checkForMissingLoginParameters(AuthenticationRequest loginData) throws MissingParameterException {
    List<String> missingParameterList = new ArrayList<>();
    userService.checkIfNullOrEmptyField(loginData.getUsername(), "username", missingParameterList);
    userService.checkIfNullOrEmptyField(loginData.getPassword(), "password", missingParameterList);
    if (missingParameterList.size() > 0) {
      throw new MissingParameterException(missingParameterList);
    }
  }


  public Boolean isUsernameMissing(AuthenticationRequest loginRequest) {
    return (loginRequest.getUsername() == null || loginRequest.getUsername().equals(""));
  }

  public Boolean isPasswordMissing(AuthenticationRequest loginRequest) {
    return (loginRequest.getPassword() == null || loginRequest.getPassword().equals(""));
  }

}
