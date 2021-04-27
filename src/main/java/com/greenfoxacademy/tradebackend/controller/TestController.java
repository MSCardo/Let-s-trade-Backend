package com.greenfoxacademy.tradebackend.controller;

import com.greenfoxacademy.tradebackend.model.Hello;
import com.greenfoxacademy.tradebackend.service.HelloService;
import com.greenfoxacademy.tradebackend.service.retrofit.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class TestController {

  private HelloService helloService;
  private StockService stockService;

  @Autowired
  public TestController(HelloService helloService,
                        StockService stockService) {
    this.helloService = helloService;
    this.stockService = stockService;
  }


  @GetMapping("/hello")
  public ResponseEntity<Hello> hello(){
    return ResponseEntity.ok(helloService.helloWorld());
  }

  @GetMapping("/token")
  public ResponseEntity<Hello> helloToken(@RequestHeader("token")String token){
    return ResponseEntity.ok(helloService.helloToken());
  }
//
//  @GetMapping("/stock/{symbol}")
//  public ResponseEntity<StockAPI> stockQuote(@PathVariable String symbol){
//    StockAPI stock = stockService.getStockQuote(symbol);
//    return ResponseEntity.ok(stock);
//  }


}
