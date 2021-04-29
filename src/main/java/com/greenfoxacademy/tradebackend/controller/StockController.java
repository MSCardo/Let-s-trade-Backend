package com.greenfoxacademy.tradebackend.controller;

import com.greenfoxacademy.tradebackend.exception.exception.*;
import com.greenfoxacademy.tradebackend.model.stock.*;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.service.UserService;
import com.greenfoxacademy.tradebackend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
public class StockController {

  private StockService stockService;
  private UserService userService;

  @Autowired
  public StockController(StockService stockService, UserService userService) {
    this.stockService = stockService;
    this.userService = userService;
  }

  @GetMapping("/stock")
  public ResponseEntity<StockStatusDTO> actualStocks(@RequestHeader(value = "token") String token)
      throws NoSuchUserException {
    User user = userService.getUserByToken(token);
    List<Stock> stocks = stockService.getUpdatedStocks(user);
    return ResponseEntity.ok(new StockStatusDTO(user.getBalance(),stockService.stockToResponseDTO(stocks)));
  }

  @PostMapping("/stock")
  public ResponseEntity<StockStatusDTO> buyStocks(@RequestBody StockRequestDTO stockRequestDTO, @RequestHeader(value = "token") String token)
      throws NoSuchUserException, InsufficientBalanceException, NoSuchStockException, InvalidAmountException {
    return new ResponseEntity<>(stockService.buyStock(stockRequestDTO.getSymbol(), stockRequestDTO.getAmount(), userService.getUserByToken(token)), HttpStatus.CREATED);
  }

  @PutMapping("/stock")
  public ResponseEntity<StockStatusDTO> sellStocks(@RequestBody StockRequestDTO stockRequestDTO, @RequestHeader(value = "token") String token)
      throws NoSuchUserException, NoSuchStockException, InvalidAmountException {
    return new ResponseEntity<>(stockService.sellStock(stockRequestDTO.getSymbol(), stockRequestDTO.getAmount(), userService.getUserByToken(token)), HttpStatus.OK);
  }

  @PostMapping("/scheduled")
  public ResponseEntity<List<ScheduledStockResponseDTO>> scheduleAction(@RequestBody ScheduledStockRequestDTO scheduledStockRequestDTO, @RequestHeader(value = "token") String token) throws NoSuchUserException, InvalidActionException, InvalidAmountException, NoSuchStockException, InvalidTimeException {
    stockService.saveScheduledAction(userService.getUserByToken(token), scheduledStockRequestDTO.getSymbol(), scheduledStockRequestDTO.getAmount(), scheduledStockRequestDTO.getTimestamp(), scheduledStockRequestDTO.getAction());
    return new ResponseEntity<>(stockService.scheduledStockToResponseDTO(userService.getUserByToken(token).getScheduledStock()), HttpStatus.CREATED);
  }
}
