package com.greenfoxacademy.tradebackend.model.login;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.greenfoxacademy.tradebackend.security.Jwt.AuthenticationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

  private String status;
  private String message;
  private String token;

  public LoginResponseDTO(AuthenticationResponse authenticationResponse) {
    this.status = "ok";
    this.token = authenticationResponse.getJwt();
  }

  public LoginResponseDTO(String message) {
    this.status = "error";
    this.message = message;
  }
}
