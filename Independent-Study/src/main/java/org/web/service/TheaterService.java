//package org.web.service;
//
//import org.springframework.stereotype.Service;
//import org.web.dao.TheaterDao;
//import org.web.dto.Result;
//import org.web.dto.TheaterDto;
//import org.web.entity.Theater;
//
//import java.util.Base64;
//import java.util.Optional;
//
//@Service
//public class TheaterService {
//
//    private TheaterDao theaterDao;
//
//    private TheaterDto convertToDto(Theater theater){
//        String imageBase64 = Base64.getEncoder().encodeToString(theater.getImage());
//        return new TheaterDto(
//                theater.getTheaterId(),
//                theater.getTheaterName(),
//                theater.getArea(),
//                theater.getAddress(),
//                theater.getTel(),
//                imageBase64
//        );
//    }
//
//    private Theater convertToEntity(TheaterDto theaterDto){
//        byte[] imageBytes = Base64.getDecoder().decode(theaterDto.getImage());
//        return new Theater(
//                theaterDto.getTheaterId(),
//                theaterDto.getTheaterName(),
//                theaterDto.getArea(),
//                theaterDto.getAddress(),
//                theaterDto.getTel(),
//                imageBytes,
//                null
//        );
//    }
//
//    public TheaterService(TheaterDao theaterDao) {
//        this.theaterDao = theaterDao;
//    }
//
//    public Result createTheater(TheaterDto theaterDto){
//        Theater theater = convertToEntity(theaterDto);
//        theaterDao.save(theater);
//        return new Result(200, "success");
//    }
//
//    public Result updateTheater(TheaterDto theaterDto){
//        Theater theater = convertToEntity(theaterDto);
//        theaterDao.save(theater);
//        return new Result(200, "success");
//    }
//
//    public Result deleteTheater(Integer theaterId){
//        theaterDao.deleteById(theaterId);
//        return new Result(200, "success");
//    }
//
//    public Result getTheater(Integer theaterId){
//        Optional<Theater> optionalTheater = theaterDao.findById(theaterId);
//        if(optionalTheater.isPresent()){
//            TheaterDto theaterDto = convertToDto(optionalTheater.get());
//            return new Result(200, theaterDto);
//        }else {
//            return new Result(999,"no data");
//        }
//    }
//}
