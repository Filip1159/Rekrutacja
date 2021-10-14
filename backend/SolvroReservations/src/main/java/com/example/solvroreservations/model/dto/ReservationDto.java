package com.example.solvroreservations.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReservationDto {
    private Integer tableNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private int duration;

    private int seatNumber;

    private String fullName;
    private String phone;
    private String email;

    private int numberOfSeats;
}
