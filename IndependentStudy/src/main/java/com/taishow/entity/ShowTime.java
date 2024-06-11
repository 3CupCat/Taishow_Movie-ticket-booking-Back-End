package com.taishow.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "showtime", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screen_id", "showtime"})
})
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "screen_id")
    private Integer screenId;

    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "showtime")
    private Date showTime;

    public ShowTime(Integer id, Integer screenId, Integer movieId, Date showTime) {
        this.id = id;
        this.screenId = screenId;
        this.movieId = movieId;
        this.showTime = showTime;
    }
}
