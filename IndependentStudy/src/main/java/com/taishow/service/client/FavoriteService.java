package com.taishow.service.client;

import com.taishow.dao.FavoriteDao;
import com.taishow.entity.Favorite;
import com.taishow.entity.Movie;
import com.taishow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Map<String, Object>> getFavoriteByToken(String token) {
        Integer userId = jwtUtil.getUserIdFromToken(token);
        List<Object[]> results = favoriteDao.findFavoritesByUserId(userId);

        List<Map<String, Object>> favoriteList = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> favoriteMap = new HashMap<>();

            // Assuming result array has objects in specific order
            Favorite favorite = (Favorite) result[0];
            Movie movie = (Movie) result[1];
            Double avgScore = (Double) result[2];
            favoriteMap.put("favorite", favorite);
            favoriteMap.put("movie", movie);
            favoriteMap.put("avgScore", avgScore);

            favoriteList.add(favoriteMap);
        }
        return favoriteList;
    }
}
