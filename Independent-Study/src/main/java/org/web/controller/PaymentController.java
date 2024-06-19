//package org.web.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.web.dto.Result;
//import org.web.entity.Payments;
//import org.web.service.PaymentService;
//
//@RestController
//@RequestMapping("/payments")
//public class PaymentController {
//
//    private PaymentService paymentService;
//
//    public PaymentController(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    @PostMapping("/createPayment")
//    public Result createPayment(@RequestBody Payments payments){
//        return paymentService.createPayment(payments);
//    }
//
//    @PutMapping("/updatePayment")
//    public Result updatePayment(@RequestBody Payments payments){
//        return paymentService.updatePayment(payments);
//    }
//
//    // 接受前端請求後，doGet/doPost/doPut => return 執行Service商業邏輯
//    @GetMapping("/getAll")
//    public Result findAll(){
//        return paymentService.findAll();
//    }
//
//    @GetMapping("/getPayment/{orderNum}")
//    public Result getPaymentByOrderNum(@PathVariable String orderNum){
//        return paymentService.getPaymentByOrderNum(orderNum);
//    }
//}
