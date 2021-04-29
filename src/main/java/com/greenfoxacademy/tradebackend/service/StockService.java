package com.greenfoxacademy.tradebackend.service;

import com.greenfoxacademy.tradebackend.exception.exception.InsufficientBalanceException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidActionException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidAmountException;
import com.greenfoxacademy.tradebackend.exception.exception.InvalidTimeException;
import com.greenfoxacademy.tradebackend.exception.exception.NoSuchStockException;
import com.greenfoxacademy.tradebackend.model.stock.ScheduledStock;
import com.greenfoxacademy.tradebackend.model.stock.ScheduledStockResponseDTO;
import com.greenfoxacademy.tradebackend.model.stock.Stock;
import com.greenfoxacademy.tradebackend.model.stock.StockAPI;
import com.greenfoxacademy.tradebackend.model.stock.StockFactory;
import com.greenfoxacademy.tradebackend.model.stock.StockResponseDTO;
import com.greenfoxacademy.tradebackend.model.stock.StockStatusDTO;
import com.greenfoxacademy.tradebackend.model.user.User;
import com.greenfoxacademy.tradebackend.repository.ScheduledStockRepository;
import com.greenfoxacademy.tradebackend.repository.StockRepository;
import com.greenfoxacademy.tradebackend.service.retrofit.RetrofitClientInstance;
import com.greenfoxacademy.tradebackend.service.retrofit.StockDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

  private StockRepository stockRepository;
  private ScheduledStockRepository scheduledStockRepository;
  private StockFactory stockFactory;
  private UserService userService;

  @Autowired
  public StockService(StockRepository stockRepository, ScheduledStockRepository scheduledStockRepository,
                      StockFactory stockFactory, UserService userService) {
    this.stockRepository = stockRepository;
    this.scheduledStockRepository = scheduledStockRepository;
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
    buyStockLogic(symbol, amount, user);
    return new StockStatusDTO(user.getBalance(), stockToResponseDTO(user.getStock()));
  }

  public void scheduledBuyStock(String symbol, Integer amount, User user)
      throws InsufficientBalanceException, NoSuchStockException, InvalidAmountException {
    buyStockLogic(symbol, amount, user);
  }

  private void buyStockLogic(String symbol, Integer amount, User user)
      throws InvalidAmountException, NoSuchStockException, InsufficientBalanceException {
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
  }


  public List<StockResponseDTO> stockToResponseDTO(List<Stock> stockList) {
    return stockList.stream()
        .map(s -> new StockResponseDTO(s.getAmount(), s.getBuyPrice(), s.getLatestPrice(), s.getSymbol(),
            s.getTimestamp()))
        .collect(Collectors.toList());
  }

  public Integer getAllAmountBySymbol(String symbol, User user) throws NoSuchStockException {
    List<Stock> stockList = user.getStock();
    int counter = 0;
    for (Stock stock : stockList) {
      if (stock.getType().equalsIgnoreCase(symbol)) {
        counter = counter + stock.getAmount();
      }
    }
    return counter;
  }

  public Integer getAllAmountBySymbolAndList(String symbol, List<Stock> stockList) throws NoSuchStockException {
    int counter = 0;
    for (Stock stock : stockList) {
      if (stock.getType().equalsIgnoreCase(symbol)) {
        counter = counter + stock.getAmount();
      }
    }
    return counter;
  }

  private void isTimestampValid(Timestamp timestamp) throws InvalidTimeException {
    Date date = new Date();
    Timestamp actualTimestamp = new Timestamp(date.getTime());
    if (timestamp.before(actualTimestamp)) {
      throw new InvalidTimeException();
    }
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

  private void isActionValid(String action) throws InvalidActionException {
    if (!action.equalsIgnoreCase("buy") && !action.equalsIgnoreCase("sell")) {
      throw new InvalidActionException();
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

  public StockStatusDTO sellStock(String symbol, Integer amount, User user)
      throws NoSuchStockException, InvalidAmountException {
    isAmountValid(amount);
    isSymbolValid(symbol);
    sellStockLogic(symbol, amount, user);
    return new StockStatusDTO(user.getBalance(), stockToResponseDTO(user.getStock()));
  }

  private void sellStockLogic(String symbol, Integer amount, User user)
      throws NoSuchStockException, InvalidAmountException {
    Integer globalAmount = getAllAmountBySymbol(symbol, user);
    if (globalAmount - amount < 0) {
      throw new InvalidAmountException();
    }
    Integer remainingAmount = globalAmount - amount;
    user.setStock(updateList(remainingAmount, symbol, user.getStock()));
    Double profit = getStockQuote(symbol).getLatestPrice() * amount;
    user.setBalance(user.getBalance() + profit);
    userService.saveUser(user);
  }

  public void scheduledSellStock(String symbol, Integer amount, User user)
      throws NoSuchStockException, InvalidAmountException {

    List<Stock>stockList =getStocksByUser(user);
    Integer globalAmount = getAllAmountBySymbolAndList(symbol, stockList);
    if (globalAmount - amount < 0) {
      throw new InvalidAmountException();
    }
    Integer remainingAmount = globalAmount - amount;
    user.setStock(updateList(remainingAmount, symbol, stockList));
    Double profit = getStockQuote(symbol).getLatestPrice() * amount;
    user.setBalance(user.getBalance() + profit);
    userService.saveUser(user);

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

  public List<ScheduledStockResponseDTO> scheduledStockToResponseDTO(List<ScheduledStock> scheduledStockList) {
    return scheduledStockList.stream()
        .map(s -> new ScheduledStockResponseDTO(s.getSymbol(), s.getAmount(), s.getTimestamp(), s.getAction()))
        .collect(Collectors.toList());
  }

  public void saveScheduledAction(User user, String symbol, Integer amount, Timestamp timestamp, String action)
      throws NoSuchStockException, InvalidAmountException, InvalidActionException, InvalidTimeException {
    isSymbolValid(symbol);
    isAmountValid(amount);
    isActionValid(action);
    isTimestampValid(timestamp);
    scheduledStockRepository.save(new ScheduledStock(user, symbol, amount, timestamp, action));
  }

  //  @Scheduled(cron = "0 0 0 ? * MON-FRI *")
  @Scheduled(fixedRate = 5000)
  public void doScheduledAction() throws InsufficientBalanceException, InvalidAmountException, NoSuchStockException {
    System.out.println("FUT!");
    List<ScheduledStock> scheduledStockList = (List<ScheduledStock>) scheduledStockRepository.findAll();
    Date date = new Date();
    Timestamp timestamp = new Timestamp(date.getTime());
    for (ScheduledStock scheduledStock : scheduledStockList) {
      if (scheduledStock.getTimestamp().before(timestamp)) {
        if (scheduledStock.getAction().equalsIgnoreCase("buy")) {
          scheduledBuyStock(scheduledStock.getSymbol(), scheduledStock.getAmount(), scheduledStock.getUser());
          scheduledStockRepository.delete(scheduledStock);
        } else if (scheduledStock.getAction().equalsIgnoreCase("sell")) {
          scheduledSellStock(scheduledStock.getSymbol(), scheduledStock.getAmount(), scheduledStock.getUser());
          scheduledStockRepository.delete(scheduledStock);
        }
      }
    }
  }

}
