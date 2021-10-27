package com.example.solvroreservations.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Filip Wisniewski
 * Data transfer object for Reservation entity
 * Hidden fields are: id, status and cancellationToken
 */
@Getter
@Setter
@Builder
public class ReservationDto {
    private Integer tableNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime date;
    private int duration;

    private String fullName;
    private String phone;
    private String email;

    private int numberOfSeats;
}
