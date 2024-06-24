package org.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.entity.Favorite;

import java.util.List;

@Repository
public interface FavoriteDao extends JpaRepository<Favorite, Integer> {

    @Query("SELECT f, m, AVG(r.score) as avgScore FROM Favorite f " +
            "LEFT JOIN Movie m ON f.movieId = m.id " +
            "LEFT JOIN Review r ON m.id = r.movieId " +
            "WHERE f.userId = :userId " +
            "GROUP BY f, m")
    List<Object[]> findFavoritesByUserId(@Param("userId") int userId);
}
