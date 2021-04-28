package com.greenfoxacademy.tradebackend.service.retrofit;

import com.greenfoxacademy.tradebackend.exception.exception.InsufficientBalanceException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidAmountException;
import com.greenfoxacademy.tradebackend.exception.exception.NoSuchStockException;
import com.greenfoxacademy.tradebackend.model.stock.Stock;
import com.greenfoxacademy.tradebackend.model.stock.StockAPI;
import com.greenfoxacademy.tradebackend.model.stock.StockFactory;
import com.greenfoxacademy.tradebackend.model.stock.StockResponseDTO;
import com.greenfoxacademy.tradebackend.model.stock.StockStatusDTO;
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


  public StockStatusDTO buyStock(String symbol, Integer amount, User user)
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
    getUpdatedStocks(user);
    return new StockStatusDTO(user.getBalance(),stockToResponseDTO(user.getStock()));
  }


  public List<StockResponseDTO> stockToResponseDTO(List<Stock> stockList) {
    return stockList.stream()
        .map(s -> new StockResponseDTO(s.getAmount(), s.getBuyPrice(), s.getLatestPrice(), s.getSymbol(), s.getTimestamp()))
        .collect(Collectors.toList());
  }

  public Integer getAllAmountBySymbol(String symbol, User user) throws NoSuchStockException {
    Integer counter = 0;
    List<Stock> stockList = user.getStock();
    for (int i = 0; i < stockList.size(); i++) {
      if (stockList.get(i).getType().equalsIgnoreCase(symbol)){
        counter = counter + stockList.get(i).getAmount();
      }
    }
    return counter;
  }

  private void isSymbolValid(String symbol) throws NoSuchStockException {
    if (symbol == null ||
        !symbol.equalsIgnoreCase("GOOG") && !symbol.equalsIgnoreCase("MSFT") && !symbol.equalsIgnoreCase("TWTR") &&
            !symbol.equalsIgnoreCase("FB")) {
      throw new NoSuchStockException();
    }
  }

  private void isAmountValid(Integer amount) throws InvalidAmountException {
    if (amount == null || amount <= 0) {
      throw new InvalidAmountException();
    }
  }

  public List<Stock> getStocksByUser(User user) {
    return stockRepository.findStocksByUser(user);
  }


  public List<Stock> getUpdatedStocks(User user) {
    List<Stock> stockList = getStocksByUser(user);
    Double googPrice = getStockQuote("GOOG").getLatestPrice();
    Double msftPrice = getStockQuote("MSFT").getLatestPrice();
    Double twtrPrice = getStockQuote("TWTR").getLatestPrice();
    Double fbPrice = getStockQuote("FB").getLatestPrice();
    for (Stock stock : stockList) {
      switch (stock.getSymbol()) {
        case "GOOG":
          stock.setLatestPrice(googPrice);
          stockRepository.save(stock);
          break;
        case "MSFT":
          stock.setLatestPrice(msftPrice);
          stockRepository.save(stock);
          break;
        case "TWTR":
          stock.setLatestPrice(twtrPrice);
          stockRepository.save(stock);
          break;
        case "FB":
          stock.setLatestPrice(fbPrice);
          stockRepository.save(stock);
          break;
      }
    }
    return stockList;
  }

  public StockStatusDTO sellStock(String symbol, Integer amount, User user) throws NoSuchStockException, InvalidAmountException {
    isAmountValid(amount);
    isSymbolValid(symbol);
    Integer globalAmount = getAllAmountBySymbol(symbol, user);
    if (globalAmount - amount < 0) {
      throw new InvalidAmountException();
    }
    Integer remainingAmount = globalAmount - amount;
    user.setStock(updateList(remainingAmount, symbol, user.getStock()));
    Double profit = getStockQuote(symbol).getLatestPrice() * amount;
    user.setBalance(user.getBalance()+profit);
    userService.saveUser(user);
    return new StockStatusDTO(user.getBalance(), stockToResponseDTO(user.getStock()));
  }


  public List<Stock> updateList(Integer remainingAmount, String symbol, List<Stock> stockList) {
    for (int i = 0; i <= stockList.size() - 1; i++) {
      if (stockList.get(i).getType().equalsIgnoreCase(symbol)) {
        if (remainingAmount <= 0) {
        stockRepository.delete(stockList.get(i));
        stockList.remove(i);
        i--;
        } else {
          if (remainingAmount - stockList.get(i).getAmount() < 0) {
            stockList.get(i).setAmount(remainingAmount);
            remainingAmount = 0;
            stockRepository.save(stockList.get(i));
          } else {
            remainingAmount = remainingAmount - stockList.get(i).getAmount();
          }
        }
      }
    }
    return stockList;
  }
}
