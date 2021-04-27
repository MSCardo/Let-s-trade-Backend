package com.greenfoxacademy.tradebackend.service.retrofit;

import com.greenfoxacademy.tradebackend.exception.exception.InsufficientBalanceException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidAmountException;
import com.greenfoxacademy.tradebackend.exception.exception.NoSuchStockException;
import com.greenfoxacademy.tradebackend.model.stock.Stock;
import com.greenfoxacademy.tradebackend.model.stock.StockAPI;
import com.greenfoxacademy.tradebackend.model.stock.StockFactory;
import com.greenfoxacademy.tradebackend.model.stock.StockResponseDTO;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.repository.StockRepository;
import com.greenfoxacademy.tradebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

  private StockRepository stockRepository;
  private StockFactory stockFactory;
  private UserService userService;

  @Autowired
  public StockService(StockRepository stockRepository,
                      StockFactory stockFactory,
                      UserService userService) {
    this.stockRepository = stockRepository;
    this.stockFactory = stockFactory;
    this.userService = userService;
  }

  public StockAPI getStockQuote(String symbol) {
    Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
    StockDatabase stockDatabase = retrofitInstance.create(StockDatabase.class);
    Call<StockAPI> call = stockDatabase.callQuote(symbol, System.getenv("API_KEY"));
    StockAPI stock = null;
    try {
      Response<StockAPI> stockResponse = call.execute();
      stock = stockResponse.body();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stock;
  }


  public List<StockResponseDTO> buyStock(String symbol, Integer amount, User user)
      throws InsufficientBalanceException, NoSuchStockException, InvalidAmountException {
    isAmountValid(amount);
    isSymbolValid(symbol);
    Double price = getStockQuote(symbol).getLatestPrice() * amount;
    if (user.getBalance() - price < 0) {
      throw new InsufficientBalanceException();
    }
    user.setBalance(user.getBalance() - price);
    StockAPI stockAPI = getStockQuote(symbol);
    userService.saveUser(user);
    stockRepository.save(stockFactory.createStock(stockAPI, symbol, user, amount));

    return stockToResponseDTO(user.getStock());
  }



  public List<StockResponseDTO> stockToResponseDTO(List<Stock> stockList) {
    return stockList.stream()
        .map(s -> new StockResponseDTO(s.getAmount(), s.getLatestPrice(), s.getSymbol()))
        .collect(Collectors.toList());
  }

  public Integer getAllAmountBySymbol(String symbol, List<Stock> stockList) throws NoSuchStockException {
    Integer counter = 0;
    for (Stock stock : stockList) {
      if (symbol.equalsIgnoreCase(stock.getType())) {
        counter += stock.getAmount();
      }
    }
    return counter;
  }

  private void isSymbolValid(String symbol) throws NoSuchStockException {
    if (symbol == null || !symbol.equalsIgnoreCase("GOOG") && !symbol.equalsIgnoreCase("MSFT") && !symbol.equalsIgnoreCase("TWTR") && !symbol.equalsIgnoreCase("FB")) {
      throw new NoSuchStockException();
    }
  }

  private void isAmountValid(Integer amount) throws InvalidAmountException {
    if (amount == null || amount <= 0) {
      throw new InvalidAmountException();
    }
  }


}
