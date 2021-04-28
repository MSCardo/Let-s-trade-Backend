package com.greenfoxacademy.tradebackend.model.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockStatusDTO {

  private Double balance;
  private List<StockResponseDTO> stockList;
}
