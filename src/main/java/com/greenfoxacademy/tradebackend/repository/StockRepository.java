package com.greenfoxacademy.tradebackend.repository;

import com.greenfoxacademy.tradebackend.model.stock.Stock;
import com.greenfoxacademy.tradebackend.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock, Long> {

  List<Stock> findStocksByUser_Id(Long userId);

}
