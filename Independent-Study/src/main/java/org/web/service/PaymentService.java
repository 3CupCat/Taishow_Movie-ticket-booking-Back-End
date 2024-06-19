//package org.web.service;
//
//import org.springframework.stereotype.Service;
//import org.web.dao.PaymentsDao;
//import org.web.dto.Result;
//import org.web.entity.Payments;
//
//import java.util.List;
//
//@Service
//public class PaymentService {
//
//    private PaymentsDao paymentsDao;
//
//    public PaymentService(PaymentsDao paymentsDao) {
//        this.paymentsDao = paymentsDao;
//    }
//
//    public Result createPayment(Payments payments){
//        paymentsDao.save(payments);
//        return new Result(200, "success");
//    }
//
//    public Result updatePayment(Payments payments){
//        paymentsDao.save(payments);
//        return new Result(200, "success");
//    }
//
//    // 注意findAll 需傳回List<> 資料型態
//    public Result findAll(){
//        List<Payments> payments = paymentsDao.findAll();
//        return new Result(200, payments);
//    }
//
//    public Result getPaymentByOrderNum(String orderNum){
//        List<Payments> list = paymentsDao.findByOrderNum(orderNum);
//        return new Result(200, list);
//    }
//}
