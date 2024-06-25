package com.taishow.service.client;

import com.taishow.dao.BonusDao;
import com.taishow.entity.*;
import com.taishow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BonusService {

    @Autowired
    private BonusDao bonusDao;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Map<String, Object>> getBonusByToken(String token) {
        Integer userId = jwtUtil.getUserIdFromToken(token);
        List<Object[]> results = bonusDao.findByAll(userId);

        List<Map<String, Object>> orderPaymentTicketShowtimeMovieScreenTheaterBonusList = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> orderPaymentTicketShowtimeMovieScreenTheaterBonusMap = new HashMap<>();

            Orders order = (Orders) result[0];
            Payment payment = (Payment) result[1];
            Tickets ticket = (Tickets) result[2];
            SeatStatus seatStatus = (SeatStatus) result[3]; // 修改这里
            ShowTime showtime = (ShowTime) result[4];
            Movie movie = (Movie) result[5];
            Screen screen = (Screen) result[6];
            Theaters theater = result.length > 7 && result[7] instanceof Theaters ? (Theaters) result[7] : null;
            Bonus bonus = result.length > 8 && result[8] instanceof Bonus ? (Bonus) result[8] : null;

            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("order", order);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("payment", payment);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("ticket", ticket);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("seatStatus", seatStatus); // 添加 seatStatus
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("showtime", showtime);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("movie", movie);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("screen", screen);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("theater", theater);
            orderPaymentTicketShowtimeMovieScreenTheaterBonusMap.put("bonus", bonus);

            orderPaymentTicketShowtimeMovieScreenTheaterBonusList.add(orderPaymentTicketShowtimeMovieScreenTheaterBonusMap);
        }
        return orderPaymentTicketShowtimeMovieScreenTheaterBonusList;
    }
}
