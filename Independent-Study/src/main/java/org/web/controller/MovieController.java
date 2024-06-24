//package org.web.controller;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.*;
//import org.web.dto.Result;
//import org.web.entity.Movies;
//import org.web.service.MovieService;
//
//import java.sql.Date;
//import java.time.LocalDate;
//
//@RestController
//@RequestMapping("/movie")
//public class MovieController {
//    private MovieService movieService;
//
//    public MovieController(MovieService movieService) {
//        this.movieService = movieService;
//    }
//
//    @PostMapping("/createMovie")
//    public Result createMovie(@RequestParam String title,
//                              @RequestParam String titleEnglish,
//                              @RequestParam String rating,
//                              @RequestParam String runtime,
//                              @RequestParam String genre,
//                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
//                              @RequestParam String director,
//                              @RequestParam String synopsis,
//                              @RequestParam String language,
//                              @RequestParam String trailer,
//                              @RequestParam String poster,
//                              @RequestParam Boolean isOutTheater){
//        Movies movies = new Movies();
//        movies.setTitle(title);
//        movies.setTitleEnglish(titleEnglish);
//        movies.setRating(rating);
//        movies.setRuntime(runtime);
//        movies.setGenre(genre);
//        movies.setReleaseDate(releaseDate);
//        movies.setDirector(director);
//        movies.setSynopsis(synopsis);
//        movies.setLanguage(language);
//        movies.setTrailer(trailer);
//        movies.setPoster(poster);
//        movies.setOutTheater(isOutTheater);
//        return movieService.createMovie( movies);
//    }
//
//    @PutMapping("/updateMovie")
//    public Result updateMovie(@RequestBody Movies movie){
//        return movieService.updateMovie(movie);
//    }
//
//    @DeleteMapping("/deleteMovie")
//    public Result deleteMovie(@PathVariable Integer id){
//        return movieService.deleteMovie(id);
//    }
//
//    @GetMapping("/getMovie/{id}")
//    public Result getMovie(@PathVariable Integer id){
//        return movieService.getMovie(id);
//    }
//
//    @GetMapping("/getMovie/isComing")
//    public Result getMoviesByIsComing(){
//        return movieService.getMoviesByIsComing();
//    }
//
//    @GetMapping("getMovie/isPlaying")
//    public Result getMoviesByIsPlaying(){
//        return movieService.getMoviesByIsPlaying();
//    }
//
////    @GetMapping("getMovie/isPlaying")
////    public Result getMoviesByIsPlaying(){
////        return movieService.getMoviesByIsPlaying();
////    }
////
////    @GetMapping("/getMovie/isComing")
////    public Result getMoviesIsComing(){
////        return movieService.getMoviesIsComing();
////    }
////
////    @GetMapping("/getMovie/isOutTheater")
////    public Result getMoviesIsOutTheater(){
////        return movieService.getMoviesIsOutTheater();
////    }
//}
