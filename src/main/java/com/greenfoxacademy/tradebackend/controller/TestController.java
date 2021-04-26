package com.greenfoxacademy.tradebackend.controller;

import com.greenfoxacademy.tradebackend.model.Hello;
import com.greenfoxacademy.tradebackend.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class TestController {

  private HelloService helloService;

  @Autowired
  public TestController(HelloService helloService) {
    this.helloService = helloService;
  }

  @GetMapping("/hello")
  public ResponseEntity<Hello> hello(){
    return ResponseEntity.ok(helloService.helloWorld());
  }

  @GetMapping("/token")
  public ResponseEntity<Hello> helloToken(@RequestHeader("token")String token){
    return ResponseEntity.ok(helloService.helloToken());
  }
}
