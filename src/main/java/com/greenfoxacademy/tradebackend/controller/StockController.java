package com.greenfoxacademy.tradebackend.controller;

import com.greenfoxacademy.tradebackend.exception.exception.InsufficientBalanceException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidAmountException;
import com.greenfoxacademy.tradebackend.exception.exception.NoSuchStockException;
import com.greenfoxacademy.tradebackend.exception.exception.NoSuchUserException;
import com.greenfoxacademy.tradebackend.model.stock.StockRequestDTO;
import com.greenfoxacademy.tradebackend.model.stock.StockResponseDTO;
import com.greenfoxacademy.tradebackend.service.UserService;
import com.greenfoxacademy.tradebackend.service.retrofit.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StockController {

  private StockService stockService;
  private UserService userService;

  public StockController(StockService stockService, UserService userService) {
    this.stockService = stockService;
    this.userService = userService;
  }

  @PostMapping("/stock")
  public ResponseEntity<List<StockResponseDTO>> buyStocks(@RequestBody StockRequestDTO stockRequestDTO, @RequestHeader(value = "token") String token)
      throws NoSuchUserException, InsufficientBalanceException, NoSuchStockException, InvalidAmountException {
    return new ResponseEntity<>(stockService.buyStock(stockRequestDTO.getSymbol(), stockRequestDTO.getAmount(), userService.getUserByToken(token)), HttpStatus.CREATED);
  }
}
