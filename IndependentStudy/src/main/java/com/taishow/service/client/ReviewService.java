package com.taishow.service.client;

import com.taishow.dao.FavoriteDao;
import com.taishow.entity.Movie;
import com.taishow.entity.Review;
import com.taishow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Map<String, Object>> getReviewsAndMoviesByToken(String token) {
        Integer userId = jwtUtil.getUserIdFromToken(token);
        List<Object[]> results = favoriteDao.findReviewsAndMoviesByUserId(userId);

        List<Map<String, Object>> reviewList = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> reviewMap = new HashMap<>();

            // 確保順序正確：電影在第一個位置，平均分數在第二個位置
            Review review = (Review) result[0];
            Movie movie = (Movie) result[1];
            Double avgScore = (Double) result[2];

            reviewMap.put("review", review);
            reviewMap.put("movie", movie);
            reviewMap.put("avgScore", avgScore);
            reviewList.add(reviewMap);
        }
        return reviewList;
    }
}
