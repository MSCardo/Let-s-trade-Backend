package com.greenfoxacademy.tradebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TradeBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(TradeBackendApplication.class, args);
  }

}
