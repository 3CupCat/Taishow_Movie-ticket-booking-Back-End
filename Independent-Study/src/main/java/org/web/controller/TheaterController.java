//package org.web.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.web.dto.Result;
//import org.web.dto.TheaterDto;
//import org.web.entity.Theater;
//import org.web.service.TheaterService;
//
//@RestController
//@RequestMapping("/theater")
//public class TheaterController {
//
//    private TheaterService theaterService;
//
//    public TheaterController(TheaterService theaterService) {
//        this.theaterService = theaterService;
//    }
//
//    @PostMapping("/createTheater")
//    public Result createTheater(@RequestBody TheaterDto theaterDto){
//        return theaterService.createTheater(theaterDto);
//    }
//
//    @PutMapping("/updateTheater")
//    public Result updateTheater(@RequestBody TheaterDto theaterDto){
//        return theaterService.updateTheater(theaterDto);
//    }
//
//    @DeleteMapping("/deleteTheater/{theaterId}")
//    public Result deleteTheater(@PathVariable Integer theaterId){
//        return theaterService.deleteTheater(theaterId);
//    }
//
//    @GetMapping("/getTheater/{theaterId}")
//    public Result getTheater(@PathVariable Integer theaterId){
//        return theaterService.getTheater(theaterId);
//    }
//
//}
