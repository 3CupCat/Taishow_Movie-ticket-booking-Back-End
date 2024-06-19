//package org.web.dao;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.web.entity.Screen;
//
//import java.util.List;
//
//@Repository
//public interface ScreenDao extends JpaRepository<Screen, Integer> {
//
//    @Query("SELECT s FROM Screen s WHERE s.theater.theaterId = :theaterId")
//    List<Screen> findByTheaterId(@Param("theaterId") Integer theaterId);
//}
