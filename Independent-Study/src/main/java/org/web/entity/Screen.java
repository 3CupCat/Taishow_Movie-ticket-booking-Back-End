package org.web.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "screen")
public class Screen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "theater_id")
    private int theaterId;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "screen_class")
    private String screenClass;

    @Column(name = "total_row")
    private int totalRow;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenClass() {
        return screenClass;
    }

    public void setScreenClass(String screenClass) {
        this.screenClass = screenClass;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}
