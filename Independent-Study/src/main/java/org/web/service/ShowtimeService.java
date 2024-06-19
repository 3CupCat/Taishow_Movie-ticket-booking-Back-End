//package org.web.service;
//
//import org.springframework.stereotype.Service;
//import org.web.dao.ShowtimeDao;
//import org.web.dto.Result;
//import org.web.dto.ScreenDto;
//import org.web.dto.ShowtimeDto;
//import org.web.entity.Screen;
//import org.web.entity.Showtimes;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ShowtimeService {
//
//    private ShowtimeDao showtimeDao;
//
//    public ShowtimeService(ShowtimeDao showtimeDao) {
//        this.showtimeDao = showtimeDao;
//    }
//
//    // Convert Showtimes entity to ShowtimeDTO
//    private ShowtimeDto convertShowtimeToDTO(Showtimes showtimes){
//        ShowtimeDto dto = new ShowtimeDto();
//        dto.setTime(showtimes.getTime());
//        dto.setDate(showtimes.getDate());
//        dto.setMovieId(showtimes.getMovieId());
//        return dto;
//    }
//
//    // Convert Screen entity to ScreenDTO
//    private ScreenDto convertScreenToDTO(Screen screen){
//        ScreenDto dto = new ScreenDto();
//        dto.setScreenId(screen.getScreenId());
//        dto.setScreenName(screen.getScreenName());
//        dto.setScreenClass(screen.getScreenClass());
//        dto.setTotalRow(screen.getTotalRow());
//        dto.setShowtimes(screen.getShowtimes().stream().map(this::convertShowtimeToDTO).collect(Collectors.toList()));
//        return dto;
//    }
//
//    // Retrieve showtimes by theater ID and convert them to DTOs
//    public List<ShowtimeDto> getShowtimesByTheaterId(Integer theaterId){
//        List<Showtimes> showtimes = showtimeDao.findShowtimesByTheaterId(theaterId);
//        return showtimes.stream().map(this::convertShowtimeToDTO).collect(Collectors.toList());
//    }
//
//    public Result createShowtime(Showtimes showtimes){
//        showtimeDao.save(showtimes);
//        return new Result(200, "success");
//    }
//
//    public Result updateShowtime(Showtimes showtimes){
//        showtimeDao.save(showtimes);
//        return new Result(200, "success");
//    }
//
//    public Result deleteShowtime(Integer id){
//        showtimeDao.deleteById(id);
//        return new Result(200, "success");
//    }
//
//    public Result getShowtime(Integer movieId){
//        List<Showtimes> list = showtimeDao.findByMovieId(movieId);
//        return new Result(200, list);
//    }
//}
