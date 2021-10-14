package com.example.solvroreservations.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer tableNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private int duration;

    private int seatNumber;

    private String fullName;
    private String phone;
    private String email;

    private int numberOfSeats;

    private String status;
    private String cancellationToken;

    public Reservation(LocalDateTime date, int duration, int seatNumber, String fullName, String phone, String email, int numberOfSeats) {
        this.date = date;
        this.duration = duration;
        this.tableNumber = seatNumber;
        this.seatNumber = 1234;    // TODO what is the difference between seat and table number
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.numberOfSeats = numberOfSeats;
        this.status = "active";
        this.cancellationToken = String.format("%06d", (int) (Math.random() * 1000000));  // TODO why token is null
    }

}
