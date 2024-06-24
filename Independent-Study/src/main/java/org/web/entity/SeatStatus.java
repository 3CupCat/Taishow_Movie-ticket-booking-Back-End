//package org.web.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//public class SeatStatus {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer seatStatusId;
//
//    private Boolean status;
//
//    @ManyToOne
//    @JoinColumn(name = "seatId")
//    private Seat seat;
//
//    @ManyToOne
//    @JoinColumn(name = "showtimeId")
//    private Showtimes showtimes;
//
//    public SeatStatus() {
//    }
//
//    public SeatStatus(Integer seatStatusId, Boolean status, Seat seat, Showtimes showtimes) {
//        this.seatStatusId = seatStatusId;
//        this.status = status;
//        this.seat = seat;
//        this.showtimes = showtimes;
//    }
//
//    public Integer getSeatStatusId() {
//        return seatStatusId;
//    }
//
//    public void setSeatStatusId(Integer seatStatusId) {
//        this.seatStatusId = seatStatusId;
//    }
//
//    public Boolean getStatus() {
//        return status;
//    }
//
//    public void setStatus(Boolean status) {
//        this.status = status;
//    }
//
//    public Seat getSeat() {
//        return seat;
//    }
//
//    public void setSeat(Seat seat) {
//        this.seat = seat;
//    }
//
//    public Showtimes getShowtimes() {
//        return showtimes;
//    }
//
//    public void setShowtimes(Showtimes showtimes) {
//        this.showtimes = showtimes;
//    }
//
//    @Override
//    public String toString() {
//        return "SeatStatus{" +
//                "seatStatusId=" + seatStatusId +
//                ", status=" + status +
//                ", seat=" + seat +
//                ", showtimes=" + showtimes +
//                '}';
//    }
//}
