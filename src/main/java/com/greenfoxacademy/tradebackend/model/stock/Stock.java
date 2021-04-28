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
import java.util.Objects;

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
  private Double buyPrice;
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
    this.buyPrice = latestPrice;
    this.latestPrice = latestPrice;
    this.amount = amount;
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Stock stock = (Stock) o;
    return id.equals(stock.id) && symbol.equals(stock.symbol) && companyName.equals(stock.companyName) &&
        buyPrice.equals(stock.buyPrice) && latestPrice.equals(stock.latestPrice) && timestamp.equals(stock.timestamp) &&
        amount.equals(stock.amount) && user.equals(stock.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, symbol, companyName, buyPrice, latestPrice, timestamp, amount, user);
  }

  public abstract String getType();
}
