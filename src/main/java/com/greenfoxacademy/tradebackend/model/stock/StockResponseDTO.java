package com.greenfoxacademy.tradebackend.model.stock;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockResponseDTO {

  private Integer amount;
  private Double buyPrice;
  private Double latestPrice;
  private String symbol;
  private Timestamp timestamp;


}
