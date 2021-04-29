package com.greenfoxacademy.tradebackend.model.stock;

import com.greenfoxacademy.tradebackend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    private String symbol;
    private Integer amount;
    private Timestamp timestamp;
    private String action;

    public ScheduledStock(User user, String symbol, Integer amount, Timestamp timestamp, String action) {
        this.user = user;
        this.symbol = symbol;
        this.amount = amount;
        this.timestamp = timestamp;
        this.action = action;
    }
}
