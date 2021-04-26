package com.greenfoxacademy.tradebackend.service.email;

public interface EmailSender {
  void send(String to, String email);
}
