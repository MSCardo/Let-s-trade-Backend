package com.greenfoxacademy.tradebackend.model.stock;

import com.google.gson.annotations.SerializedName;
import com.greenfoxacademy.tradebackend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String symbol;
  private String companyName;
  private Double latestPrice;
  private Timestamp timestamp;
  private Integer amount;
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_id")
  private User user;

  public Stock(String symbol, String companyName, Double latestPrice, Integer amount, User user) {
    Date date = new Date();
    this.timestamp = new Timestamp(date.getTime());
    this.symbol = symbol;
    this.companyName = companyName;
    this.latestPrice = latestPrice;
    this.amount = amount;
    this.user = user;
  }

  public abstract String getType();
}
