package com.greenfoxacademy.tradebackend.service.retrofit;

import com.greenfoxacademy.tradebackend.model.stock.StockAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockDatabase {

  @GET("/stable/stock/{symbol}/quote")
  Call<StockAPI> callQuote(@Path("symbol") String symbol, @Query("token") String token);
}
