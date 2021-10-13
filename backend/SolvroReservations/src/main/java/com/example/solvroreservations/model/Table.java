package com.example.solvroreservations.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@javax.persistence.Table(name = "tables")
public class Table {
    @Id
    private Integer number;
    private Integer minNumberOfSeats;
    private Integer maxNumberOfSeats;

    @OneToMany
    @JoinColumn(name = "tableNumber")
    private List<Reservation> reservations;
}
