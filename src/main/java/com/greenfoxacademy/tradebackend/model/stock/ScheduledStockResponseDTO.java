package com.greenfoxacademy.tradebackend.model.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledStockResponseDTO {
    private String symbol;
    private Integer amount;
    private Timestamp timestamp;
    private String action;
}
