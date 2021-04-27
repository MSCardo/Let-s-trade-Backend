package com.greenfoxacademy.tradebackend.model.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {

  private Integer amount;
  private Double buyPrice;
  private String symbol;
}
