//package org.web.dao;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.web.entity.Showtimes;
//
//import java.util.List;
//
//@Repository
//public interface ShowtimeDao extends JpaRepository<Showtimes ,Integer> {
//
//    @Query("SELECT s FROM Showtimes s WHERE s.movieId = ?1")
//    List<Showtimes> findByMovieId(Integer movieId);
////    Optional<Showtimes> findByMovieId(Integer movieId); (X)
//
//    @Query("SELECT s FROM Showtimes s WHERE s.screen.theater.id = :theaterId")
//    List<Showtimes> findShowtimesByTheaterId(@Param("theaterId") Integer theaterId);
//
//}
