//package org.web.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.web.dto.Result;
//import org.web.entity.Screen;
//import org.web.service.ScreenService;
//
//@RestController
//@RequestMapping("/screen")
//public class ScreenController {
//
//    private ScreenService screenService;
//
//    public ScreenController(ScreenService screenService) {
//        this.screenService = screenService;
//    }
//
//    @PostMapping("/createScreen")
//    public Result createScreen(@RequestBody Screen screen){
//        return screenService.createScreen(screen);
//    }
//
//    @PutMapping("/updateScreen")
//    public Result updateScreen(@RequestBody Screen screen){
//        return screenService.updateScreen(screen);
//    }
//
//    @DeleteMapping("/deleteScreen/{id}")
//    public Result deleteScreen(@PathVariable Integer id){
//        return screenService.deleteScreen(id);
//    }
//
//    @GetMapping("/getScreen/{id}")
//    public Result getScreen(@PathVariable Integer id){
//        return screenService.getScreen(id);
//    }
//
//    @GetMapping("/getScreenByTheaterId/{theaterId}")
//    public Result getScreenByTheaterId(@PathVariable Integer theaterId){
//        return screenService.getScreenByTheaterId(theaterId);
//    }
//}
