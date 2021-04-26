package com.greenfoxacademy.tradebackend.exception.handler;



import com.greenfoxacademy.tradebackend.security.Jwt.ErrorEntityDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionHandler {


  @ExceptionHandler(JwtException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorEntityDTO processJwtException(JwtException ex) {
    return new ErrorEntityDTO(HttpStatus.UNAUTHORIZED, "Unauthorized request.");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorEntityDTO processIllegalArgumentException(IllegalArgumentException ex) {
    if (ex.getMessage().equals("JWT String argument cannot be null or empty.")) {
      return new ErrorEntityDTO(HttpStatus.UNAUTHORIZED, "JWT String argument cannot be null or empty.");
    }
    throw ex;
  }

}
