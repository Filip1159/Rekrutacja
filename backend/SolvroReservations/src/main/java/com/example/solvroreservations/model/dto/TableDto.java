package com.example.solvroreservations.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class TableDto {
    private int number;
    private int minNumberOfSeats;
    private int maxNumberOfSeats;
}
