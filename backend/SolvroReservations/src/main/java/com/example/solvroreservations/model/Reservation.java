package com.example.solvroreservations.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {

    @Id
    private Long id;

    private Integer tableNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date reservationStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date reservationEnd;

    private String email;
    private String status;

    public Reservation(Integer tableNumber, Date reservationStart, Date reservationEnd) {
        this.id = (long) (Math.random() * 1000);
        this.tableNumber = tableNumber;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
    }

}
