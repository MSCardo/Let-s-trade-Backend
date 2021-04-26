package com.greenfoxacademy.tradebackend.model.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConfirmationResponseDTO {
  private String username;
  private String email;
  private String confirmation;

}
