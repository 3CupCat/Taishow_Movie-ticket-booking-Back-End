//package org.web.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.web.dto.Result;
//import org.web.dto.ShowtimeDto;
//import org.web.entity.Showtimes;
//import org.web.service.ShowtimeService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/showtime")
//public class ShowtimeController {
//
//    private ShowtimeService showtimeService;
//
//    public ShowtimeController(ShowtimeService showtimeService) {
//        this.showtimeService = showtimeService;
//    }
//
//    @GetMapping("/theater/{theaterId}")
//    public List<ShowtimeDto> getShowtimesByTheaterId(@PathVariable Integer theaterId){
//        return showtimeService.getShowtimesByTheaterId(theaterId);
//    }
//
//    @PostMapping("/createShowtime")
//    public Result createShowtime(@RequestBody Showtimes showtimes){
//        return showtimeService.createShowtime(showtimes);
//    }
//
//    @PutMapping("/updateShowtime")
//    public Result updateShowtime(@RequestBody Showtimes showtimes){
//        return showtimeService.updateShowtime(showtimes);
//    }
//
//    @DeleteMapping("/deleteShowtime/{id}")
//    public Result deleteShowtime(@PathVariable Integer id){
//        return showtimeService.deleteShowtime(id);
//    }
//
//    @GetMapping("/getShowtime/{movieId}")
//    public Result getShowtime(@PathVariable Integer movieId){
//        return showtimeService.getShowtime(movieId);
//    }
//
//}
