package com.greenfoxacademy.tradebackend.model.stock;

import com.greenfoxacademy.tradebackend.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class GOOG extends Stock{

  public GOOG(String symbol, String companyName, Double latestPrice, Integer amount, User user){
    super(symbol, companyName, latestPrice, amount, user);
  }

  @Override
  public String getType() {
    return "GOOG";
  }

}
