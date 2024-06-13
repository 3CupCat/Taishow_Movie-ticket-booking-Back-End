package com.taishow.service;

import com.taishow.dao.MovieRepository;
import com.taishow.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createOrUpdateMovie(Movie movie){
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Integer id){
        return movieRepository.findById(id);
    }

    public Movie updateMovie(Integer id, Movie movieDetails) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        if (movieOptional.isPresent()) {
            Movie existingMovie = movieOptional.get();

            // Update the existing movie's details with the provided movieDetails
            if (movieDetails.getTitle() != null) {
                existingMovie.setTitle(movieDetails.getTitle());
            }
            if (movieDetails.getTitleEnglish() != null) {
                existingMovie.setTitleEnglish(movieDetails.getTitleEnglish());
            }
            if (movieDetails.getRating() != null) {
                existingMovie.setRating(movieDetails.getRating());
            }
            if (movieDetails.getGenre() != null) {
                existingMovie.setGenre(movieDetails.getGenre());
            }
            if (movieDetails.getReleaseDate() != null) {
                existingMovie.setReleaseDate(movieDetails.getReleaseDate());
            }
            if (movieDetails.getDirector() != null) {
                existingMovie.setDirector(movieDetails.getDirector());
            }
            if (movieDetails.getSynopsis() != null) {
                existingMovie.setSynopsis(movieDetails.getSynopsis());
            }
            if (movieDetails.getLanguage() != null) {
                existingMovie.setLanguage(movieDetails.getLanguage());
            }
            if (movieDetails.getTrailer() != null) {
                existingMovie.setTrailer(movieDetails.getTrailer());
            }
            if (movieDetails.getPoster() != null) {
                existingMovie.setPoster(movieDetails.getPoster());
            }


            return movieRepository.save(existingMovie);
        }

        return null; // or throw an exception if preferred
    }

    public boolean deleteMovie(Integer id){
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public List<Movie> getAllMovies(boolean isPlaying) {
        return movieRepository.findByIsPlaying(isPlaying);
    }

}