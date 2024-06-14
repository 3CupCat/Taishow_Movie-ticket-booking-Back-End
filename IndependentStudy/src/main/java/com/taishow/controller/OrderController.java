package com.taishow.controller;

import com.taishow.dto.OrderDto;
import com.taishow.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/ecpayCheckout")
    public String ecpayCheckout() {
        String aioCheckOutALLForm = orderService.ecpayCheckout();

        return aioCheckOutALLForm;
    }

    @PostMapping("/booking/{movieId}/order")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto,
                                              @PathVariable Integer movieId){
        // try catch 進service層 做交易，有問題這邊會到catch，沒問題在做金流
        try {
            orderService.createOrder(orderDto, movieId);

            return ResponseEntity.status(HttpStatus.CREATED).body("訂單Form");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
