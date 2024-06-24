package com.taishow.controller.client;

import com.taishow.dto.OrderDto;
import com.taishow.entity.TicketType;
import com.taishow.service.client.OrderService;
import ecpay.payment.integration.AllInOne;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/booking/{movieId}/order")
    public ResponseEntity<List<TicketType>> getTicketTypeDetail(@PathVariable Integer movieId){
        List<TicketType> ticketTypeList = orderService.getTicketTypeDetail(movieId);
        if (!ticketTypeList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(ticketTypeList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/booking/{movieId}/order")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto,
                                              @PathVariable Integer movieId){
        try {
            // 檢查訂單資訊
            orderService.checkOrderInformation(orderDto, movieId);

            // 訂單寫入資料庫
            Map<String, String> orderDetail = orderService.createOrder(orderDto, movieId);

            // 僅使用紅利點數購票，不須送綠界付款
            if ("0".equals(orderDetail.get("totalPrice"))){
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }

            String aioCheckOutALLForm = orderService.ecpayCheckout(orderDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body(aioCheckOutALLForm);
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("建立訂單失敗: " + e.getMessage());
        }
    }

    @PostMapping("/ecpayCallback")
    public ResponseEntity<String> handleEcpayCallback(@RequestParam Map<String, String> callbackData){

        Hashtable<String, String> hashtable = new Hashtable<>(callbackData);

        AllInOne allInOne = new AllInOne("");

        if (!allInOne.compareCheckMacValue(hashtable)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CheckMacValue");
        }

        try {
            if ("1".equals(callbackData.get("RtnCode"))){
                orderService.paymentSuccess(hashtable);
                return ResponseEntity.status(HttpStatus.OK).body("1|OK");
            } else {
                orderService.paymentFailure(hashtable);
                return ResponseEntity.status(HttpStatus.OK).body("PaymentFailure");
            }
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("建立訂單失敗: " + e.getMessage());
        }
    }
}
