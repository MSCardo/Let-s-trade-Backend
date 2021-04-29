package com.greenfoxacademy.tradebackend.service;


import com.greenfoxacademy.tradebackend.exception.exception.ReservedEMailException;
import com.greenfoxacademy.tradebackend.exception.exception.UserException;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.repository.UserRepository;
import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationToken;
import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class SignUpService {


  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ConfirmationTokenService confirmationTokenService;
  private final StockService stockService;

  @Autowired
  public SignUpService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       ConfirmationTokenService confirmationTokenService,
                       StockService stockService) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.confirmationTokenService = confirmationTokenService;
    this.stockService = stockService;
  }

  public String signUpUser(User user) throws UserException {
    boolean userExists = userRepository
        .findByEmail(user.getEmail())
        .isPresent();

    if (userExists) throw new ReservedEMailException();

    String encodedPassword = bCryptPasswordEncoder
        .encode(user.getPassword());

    user.setPassword(encodedPassword);

    user.setBalance(10000.0);

    userRepository.save(user);

    String token = UUID.randomUUID().toString();

    ConfirmationToken confirmationToken = new ConfirmationToken(
        token,
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(24),
        user
    );

    confirmationTokenService.saveConfirmationToken(
        confirmationToken);

    return token;
  }
}
