package com.greenfoxacademy.tradebackend.model.stock;

import com.greenfoxacademy.tradebackend.exception.exception.NoSuchStockException;
import com.greenfoxacademy.tradebackend.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class StockFactory {


  public Stock createStock(StockAPI stock, String symbol, User user, Integer amount) throws NoSuchStockException {
    if (symbol.equalsIgnoreCase("GOOG")) {
      return new GOOG(stock.getSymbol(), stock.getCompanyName(), stock.getLatestPrice(), amount, user);
    } else if (symbol.equalsIgnoreCase("MSFT")) {
      return new MSFT(stock.getSymbol(), stock.getCompanyName(), stock.getLatestPrice(), amount, user);
    } else if (symbol.equalsIgnoreCase("TWTR")) {
      return new TWTR(stock.getSymbol(), stock.getCompanyName(), stock.getLatestPrice(), amount, user);
    } else if (symbol.equalsIgnoreCase("FB")) {
      return new FB(stock.getSymbol(), stock.getCompanyName(), stock.getLatestPrice(), amount, user);
    } else {
      throw new NoSuchStockException();
    }
  }
}
