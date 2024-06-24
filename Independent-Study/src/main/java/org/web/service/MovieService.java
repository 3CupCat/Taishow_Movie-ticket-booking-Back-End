//package org.web.service;
//
//import org.springframework.stereotype.Service;
//import org.web.dao.MovieDao;
//import org.web.dto.Result;
//import org.web.entity.Movies;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MovieService {
//    private MovieDao movieDao;
//
//    public MovieService(MovieDao movieDao) {
//        this.movieDao = movieDao;
//    }
//
//    public Result createMovie(Movies movies){
//        Movies movie = movieDao.save(movies);
//        return new Result(200, movie);
//    }
//
//    public Result updateMovie(Movies movies){
//        Movies movie = movieDao.save(movies);
//        return new Result(200, movie);
//    }
//
//    public Result deleteMovie(Integer id){
//        movieDao.deleteById(id);
//        return new Result(200, "success");
//    }
//
//    public Result getMovie(Integer id){
//        Optional<Movies> moviesOptional = movieDao.findById(id);
//        if(moviesOptional.isPresent()){
//            return  new Result(200, moviesOptional.get());
//        }else{
//            return new Result(999, "No data");
//        }
//    }
//
//    public Result getMoviesByIsComing(){
//        LocalDate today = LocalDate.now();
//        List<Movies> movies = movieDao.findMoviesByReleaseDateAfter(today);
//        return new Result(200, movies);
//    }
//
//    public Result getMoviesByIsPlaying(){
//        LocalDate today = LocalDate.now();
//        List<Movies> movies = movieDao.findMoviesByReleaseDateBeforeAndIsOutTheaterFalse(today);
//        return new Result(200, movies);
//    }
//
////    public Result getMoviesByIsPlaying(){
////        List<Movies> movies = movieDao.findMoviesByIsPlayingTrue();
////        return new Result(200, movies);
////    }
////
////    public Result getMoviesIsComing(){
////        LocalDate today = LocalDate.now();
////        List<Movies> movies = movieDao.findMoviesByReleaseDateAfterAndIsPlayingFalse(today);
////        return new Result(200, movies);
////    }
////
////    public Result getMoviesIsOutTheater(){
////        LocalDate today = LocalDate.now();
////        List<Movies> movies = movieDao.findMoviesByReleaseDateBeforeAndIsPlayingFalse(today);
////        return new Result(200, movies);
////    }
//}
