package com.taishow.controller;

import com.taishow.dto.OrderDto;
import com.taishow.service.OrderService;
import ecpay.payment.integration.AllInOne;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.Map;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //測試金流用
//    @PostMapping("/ecpayCheckout")
//    public String ecpayCheckout() {
//        String aioCheckOutALLForm = orderService.ecpayCheckout();
//
//        return aioCheckOutALLForm;
//    }

    @PostMapping("/booking/{movieId}/order")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto,
                                              @PathVariable Integer movieId){
        try {
            Map<String, String> orderDetail = orderService.createOrder(orderDto, movieId);
            String aioCheckOutALLForm = orderService.ecpayCheckout(orderDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body(aioCheckOutALLForm);
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("建立訂單失敗: " + e.getMessage());
        }
    }

    //測試金流回調資料
    @PostMapping("/ecpayCallback")
    public ResponseEntity<String> handleEcpayCallback(@RequestBody Hashtable<String, String> callbackData){

        AllInOne allInOne = new AllInOne("");

        if (!allInOne.compareCheckMacValue(callbackData)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CheckMacValue");
        }

        if ("1".equals(callbackData.get("RtnCode"))){
            //交易成功，更新相關資料
        } else {
            //交易失敗，調整相關資料
        }

        return ResponseEntity.status(HttpStatus.OK).body("1|OK");
    }
}
