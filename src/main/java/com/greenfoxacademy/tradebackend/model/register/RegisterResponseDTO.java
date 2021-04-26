package com.greenfoxacademy.tradebackend.model.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponseDTO {
  private String message;
  private Long id;
  private String username;
  private String email;
  private String validationToken;


  public RegisterResponseDTO(String username, String email, String validationToken) {
     this.username = username;
     this.email = email;
    this.validationToken = validationToken;
  }

  public RegisterResponseDTO(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RegisterResponseDTO)) {
      return false;
    }
    RegisterResponseDTO that = (RegisterResponseDTO) o;
    return getId().equals(that.getId())
        && getUsername().equals(that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUsername());
  }
}
