package com.greenfoxacademy.tradebackend.repository;

import com.greenfoxacademy.tradebackend.model.stock.ScheduledStock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledStockRepository extends CrudRepository<ScheduledStock, Long> {
}
