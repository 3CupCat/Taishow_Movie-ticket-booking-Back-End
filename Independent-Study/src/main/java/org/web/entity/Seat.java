//package org.web.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//public class Seat {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer seatId;
//
//    private Integer rowNumber;
//    private Integer seatNumber;
//    private String  seatNote;
//    private Boolean isAisle;
//
//    @ManyToOne
//    @JoinColumn(name = "screenId")
//    private Screen screen;
//
//    public Seat() {
//    }
//
//    public Seat(Integer seatId, Integer rowNumber, Integer seatNumber, String seatNote, Boolean isAisle, Screen screen) {
//        this.seatId = seatId;
//        this.rowNumber = rowNumber;
//        this.seatNumber = seatNumber;
//        this.seatNote = seatNote;
//        this.isAisle = isAisle;
//        this.screen = screen;
//    }
//
//    public Integer getSeatId() {
//        return seatId;
//    }
//
//    public void setSeatId(Integer seatId) {
//        this.seatId = seatId;
//    }
//
//    public Integer getRowNumber() {
//        return rowNumber;
//    }
//
//    public void setRowNumber(Integer rowNumber) {
//        this.rowNumber = rowNumber;
//    }
//
//    public Integer getSeatNumber() {
//        return seatNumber;
//    }
//
//    public void setSeatNumber(Integer seatNumber) {
//        this.seatNumber = seatNumber;
//    }
//
//    public String getSeatNote() {
//        return seatNote;
//    }
//
//    public void setSeatNote(String seatNote) {
//        this.seatNote = seatNote;
//    }
//
//    public Boolean getAisle() {
//        return isAisle;
//    }
//
//    public void setAisle(Boolean aisle) {
//        isAisle = aisle;
//    }
//
//    public Screen getScreen() {
//        return screen;
//    }
//
//    public void setScreen(Screen screen) {
//        this.screen = screen;
//    }
//
//    @Override
//    public String toString() {
//        return "Seat{" +
//                "seatId=" + seatId +
//                ", rowNumber=" + rowNumber +
//                ", seatNumber=" + seatNumber +
//                ", seatNote='" + seatNote + '\'' +
//                ", isAisle=" + isAisle +
//                ", screen=" + screen +
//                '}';
//    }
//}
