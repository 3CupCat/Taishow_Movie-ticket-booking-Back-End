package org.web.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "title_english")
    private String titleEnglish;

    @Column(name = "rating")
    private String rating;

    @Column(name = "runtime")
    private int runtime;

    @Column(name = "genre")
    private String genre;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "director")
    private String director;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "language")
    private String language;

    @Column(name = "trailer")
    private String trailer;

    @Column(name = "poster")
    private String poster;

    @Column(name = "is_out_theater")
    private boolean isOutTheater;

    @Column(name = "is_playing")
    private boolean isPlaying;

    @Column(name = "is_homepage_trailer")
    private boolean isHomepageTrailer;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isOutTheater() {
        return isOutTheater;
    }

    public void setOutTheater(boolean isOutTheater) {
        this.isOutTheater = isOutTheater;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isHomepageTrailer() {
        return isHomepageTrailer;
    }

    public void setHomepageTrailer(boolean isHomepageTrailer) {
        this.isHomepageTrailer = isHomepageTrailer;
    }
}
