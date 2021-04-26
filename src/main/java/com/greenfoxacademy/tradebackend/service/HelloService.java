package com.greenfoxacademy.tradebackend.service;

import com.greenfoxacademy.tradebackend.model.Hello;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  public Hello helloWorld(){
    Hello hello = new Hello("Hello World!");
    return hello;
  }

  public Hello helloToken(){
    Hello hello = new Hello("You need a token to see this!");
    return hello;
  }

}
