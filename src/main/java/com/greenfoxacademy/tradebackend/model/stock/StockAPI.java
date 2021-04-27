package com.greenfoxacademy.tradebackend.model.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAPI {

  private String symbol;
  private String companyName;
  private Double latestPrice;
}
