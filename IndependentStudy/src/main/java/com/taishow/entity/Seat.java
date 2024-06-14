package com.taishow.entity;

import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "screen_id")
    private Integer screenId;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "seat_note")
    private String seatNote;

    @Column(name = "is_aisle")
    private boolean isAisle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScreenId() {
        return screenId;
    }

    public void setScreenId(Integer screenId) {
        this.screenId = screenId;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatNote() {
        return seatNote;
    }

    public void setSeatNote(String seatNote) {
        this.seatNote = seatNote;
    }

    public boolean isAisle() {
        return isAisle;
    }

    public void setAisle(boolean aisle) {
        isAisle = aisle;
    }
}
