package org.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.dto.Result;
import org.web.entity.Orders;
import org.web.service.OrderService;
import org.web.util.JwtUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JwtUtil jwtUtil;

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public Result createOrder(@RequestBody Orders orders){
        System.out.println(orders);
        return orderService.createOrder(orders);
    }
    @PutMapping("/updateOder")
    public Result updateOder(@RequestBody Orders orders){
        return orderService.updateOder(orders);
    }
    @DeleteMapping("/deleteOrder/{id}")
    public Result deleteOrder(@PathVariable Integer id){
        return orderService.deleteOrder(id);
    }
    @GetMapping("/getOrder/{id}")

    public Result getOrder(@PathVariable Integer id){
        return orderService.getOrder(id);
    }

    @GetMapping("/orderDetails")
    public List<Map<String, Object>> getOrderDetails(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return orderService.getOrdersPaymentsTicketsShowtimeMovieScreenAndTheaterByToken(token);
    }
    }

