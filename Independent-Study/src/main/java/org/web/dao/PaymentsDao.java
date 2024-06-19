//package org.web.dao;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.web.entity.Payments;
//
//import java.util.List;
//
//@Repository
//public interface PaymentsDao extends JpaRepository<Payments, Integer> {
//
//    @Query("SELECT p FROM Payments p WHERE p.orders.orderNum =: orderNum")
//    List<Payments> findByOrderNum (@Param("orderNum") String orderNum);
//}
