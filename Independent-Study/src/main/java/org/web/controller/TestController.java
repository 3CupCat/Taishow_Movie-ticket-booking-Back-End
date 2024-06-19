//package org.web.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.web.dao.BradDao;
//import org.web.entity.Brad;
//
//@Controller
//public class TestController {
//
//
//
//    @Autowired
//    private BradDao bradDao;
//
//
//    @ResponseBody
//    @PostMapping("/test")
//    public String upload(@RequestBody Brad brad){
//          bradDao.save(brad);
//        return "SUCCESS";
//    }
//
//    @ResponseBody
//    @GetMapping("/getImage/{id}")
//    public Brad getImage(@PathVariable Integer id){
//        return bradDao.findById(id).get();
//    }
//
//
//}
