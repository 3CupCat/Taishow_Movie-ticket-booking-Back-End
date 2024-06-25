package com.taishow.service.client;

import com.taishow.dao.OrderDao;
import com.taishow.dto.Result;
import com.taishow.entity.*;
import com.taishow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderHistoryService {
    @Autowired
    private JwtUtil jwtUtil;
    private OrderDao orderDao;

    public OrderHistoryService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public Result createOrder(Orders orders){
        orderDao.save(orders);
        return new Result(200, "success");
    }
    public Result updateOder(Orders orders){
        orderDao.save(orders);
        return new Result(200, "success");
    }
    public Result deleteOrder(Integer id){
        orderDao.deleteById(id);
        return new Result(200, "success");
    }
    public Result getOrder(Integer id){
        Optional<Orders> optionalOrders = orderDao.findById(id);
        if(optionalOrders.isPresent()){
            return new Result(200, optionalOrders.get());
        }else{
            return new Result(9999, "no data");
        }
    }

    public List<Map<String, Object>> getOrdersPaymentsTicketsShowtimeMovieScreenAndTheaterByToken(String token) {
        Integer userId = jwtUtil.getUserIdFromToken(token);
        List<Object[]> results = orderDao.findOrdersWithPaymentsTicketsShowtimeMovieScreenAndTheaterByUserId(userId);

        List<Map<String, Object>> orderPaymentTicketShowtimeMovieScreenTheaterList = new ArrayList<>();
        for (Object[] result : results) {
            // Logging the result length and contents for debugging
            System.out.println("Result length: " + result.length);
            for (Object obj : result) {
                System.out.println(obj);
            }

            Map<String, Object> orderPaymentTicketShowtimeMovieScreenTheaterMap = new HashMap<>();

            Orders order = (Orders) result[0];
            Payment payment = (Payment) result[1];
            Tickets ticket = (Tickets) result[2];
            SeatStatus seatStatus = (SeatStatus) result[3];  // 正確解析 SeatStatus
            ShowTime showtime = (ShowTime) result[4];
            Movie movie = (Movie) result[5];
            Screen screen = (Screen) result[6];
            Theaters theater = result.length > 7 && result[7] instanceof Theaters ? (Theaters) result[7] : null;

            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("order", order);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("payment", payment);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("ticket", ticket);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("seatStatus", seatStatus);  // 加入 seatStatus 到 Map 中
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("showtime", showtime);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("movie", movie);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("screen", screen);
            orderPaymentTicketShowtimeMovieScreenTheaterMap.put("theater", theater);

            orderPaymentTicketShowtimeMovieScreenTheaterList.add(orderPaymentTicketShowtimeMovieScreenTheaterMap);
        }
        return orderPaymentTicketShowtimeMovieScreenTheaterList;
    }

}
