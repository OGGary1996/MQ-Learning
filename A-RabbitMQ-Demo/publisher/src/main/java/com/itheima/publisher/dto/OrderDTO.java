package com.itheima.publisher.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderId;
    private String orderName;
    private Double orderMoney;
}
