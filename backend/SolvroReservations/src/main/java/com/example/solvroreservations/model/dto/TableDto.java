package com.example.solvroreservations.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Filip Wisniewski
 * Data transfer object for Reservation entity
 * Hidden fields are: list of reservations
 */
@Getter
@Setter
@Builder
public class TableDto {
    private int number;
    private int minNumberOfSeats;
    private int maxNumberOfSeats;
}
