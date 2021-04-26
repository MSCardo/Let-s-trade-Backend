package com.greenfoxacademy.tradebackend.exception.handler;



import com.greenfoxacademy.tradebackend.exception.exception.MissingParameterException;
import com.greenfoxacademy.tradebackend.exception.exception.NotValidEMailException;
import com.greenfoxacademy.tradebackend.exception.exception.NotValidatedUserException;
import com.greenfoxacademy.tradebackend.exception.exception.ReservedEMailException;
import com.greenfoxacademy.tradebackend.exception.exception.ReservedUsernameException;
import com.greenfoxacademy.tradebackend.model.ErrorDTO;
import com.greenfoxacademy.tradebackend.model.login.LoginResponseDTO;
import com.greenfoxacademy.tradebackend.model.register.RegisterResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(ReservedUsernameException.class)
  public ResponseEntity<RegisterResponseDTO> reservedUsernameExceptionHandling(ReservedUsernameException ex) {
    return new ResponseEntity<>(new RegisterResponseDTO("Username is already taken, please choose another one."),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MissingParameterException.class)
  public ResponseEntity<RegisterResponseDTO> missingParameterExceptionHandling(MissingParameterException ex) {
    String message = "Missing parameter(s): " + String.join(", ", ex.getMissingParameterList());
    return new ResponseEntity<>(new RegisterResponseDTO(message), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<LoginResponseDTO> usernameNotFoundExceptionHandling() {
    return new ResponseEntity<>(new LoginResponseDTO("No such user can be found!"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotValidatedUserException.class)
  public ResponseEntity<LoginResponseDTO> notValidatedUserExceptionHandling() {
    return new ResponseEntity<>(new LoginResponseDTO("Validate your e-mail first!"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotValidEMailException.class)
  public ResponseEntity<RegisterResponseDTO> notValidEMailExceptionHandling() {
    return new ResponseEntity<>(new RegisterResponseDTO("E-mail is not valid!"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<LoginResponseDTO> badCredentialsExceptionHandling() {
    return new ResponseEntity<>(new LoginResponseDTO("Wrong password!"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDTO> httpMessageNotReadableExceptionHandling(HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(new ErrorDTO("The message is not readable."),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ReservedEMailException.class)
  public ResponseEntity<RegisterResponseDTO> reservedEMailException(ReservedEMailException ex) {
    return new ResponseEntity<>(new RegisterResponseDTO("E-mail is already taken, please choose another one."),
        HttpStatus.CONFLICT);
  }
}