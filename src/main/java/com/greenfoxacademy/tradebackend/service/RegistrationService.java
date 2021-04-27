package com.greenfoxacademy.tradebackend.service;


import com.greenfoxacademy.tradebackend.exception.exception.MissingParameterException;
import com.greenfoxacademy.tradebackend.exception.exception.NotValidEMailException;
import com.greenfoxacademy.tradebackend.exception.exception.UserException;
import com.greenfoxacademy.tradebackend.model.register.ConfirmationResponseDTO;
import com.greenfoxacademy.tradebackend.model.register.RegisterResponseDTO;
import com.greenfoxacademy.tradebackend.model.register.RegistrationRequestDTO;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.model.user.UserRole;
import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationToken;
import com.greenfoxacademy.tradebackend.security.confirmationToken.ConfirmationTokenService;
import com.greenfoxacademy.tradebackend.service.email.EmailSender;
import com.greenfoxacademy.tradebackend.service.email.EmailService;
import com.greenfoxacademy.tradebackend.service.email.EmailValidator;
import com.greenfoxacademy.tradebackend.service.retrofit.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {


  private final UserService userService;
  private final SignUpService signUpService;
  private final EmailValidator emailValidator;
  private final ConfirmationTokenService confirmationTokenService;
  private final EmailSender emailSender;
  private final EmailService emailService;

  @Autowired
  public RegistrationService(UserService userService,
                             SignUpService signUpService,
                             EmailValidator emailValidator,
                             ConfirmationTokenService confirmationTokenService,
                             EmailSender emailSender,
                             EmailService emailService) {
    this.userService = userService;
    this.signUpService = signUpService;
    this.emailValidator = emailValidator;
    this.confirmationTokenService = confirmationTokenService;
    this.emailSender = emailSender;
    this.emailService = emailService;
  }

  public RegisterResponseDTO register(RegistrationRequestDTO request)
      throws UserException {
    checkForMissingRegisterParameters(request);

    boolean isValidEmail = emailValidator.
        test(request.getEmail());

    if (!isValidEmail) {
      throw new NotValidEMailException();
    }

    String token = signUpService.signUpUser(
        new User(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            UserRole.USER
        )
    );

    String link = "http://localhost:8080/register/confirm?token=" + token;
    emailSender.send(
        request.getEmail(),
        emailService.buildEmail(request.getUsername(), link));


    return new RegisterResponseDTO(request.getUsername(), request.getEmail(), token);
  }

  @Transactional
  public ConfirmationResponseDTO confirmToken(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService
        .getToken(token)
        .orElseThrow(() ->
            new IllegalStateException("token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("email already confirmed");
    }

    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("token expired");
    }

    confirmationTokenService.setConfirmedAt(token);
    userService.enableAppUser(
        confirmationToken.getUser().getEmail());

    //TODO return DTO
    return new ConfirmationResponseDTO(confirmationToken.getUser().getUsername(),
        confirmationToken.getUser().getEmail(),"confirmed");
  }


  public void checkForMissingRegisterParameters(RegistrationRequestDTO requestDTO) throws MissingParameterException {
    List<String> missingParameterList = new ArrayList<>();
    userService.checkIfNullOrEmptyField(requestDTO.getUsername(), "username", missingParameterList);
    userService.checkIfNullOrEmptyField(requestDTO.getPassword(), "password", missingParameterList);
    userService.checkIfNullOrEmptyField(requestDTO.getEmail(), "e-mail", missingParameterList);
    if (missingParameterList.size() > 0) {
      throw new MissingParameterException(missingParameterList);
    }
  }

}
