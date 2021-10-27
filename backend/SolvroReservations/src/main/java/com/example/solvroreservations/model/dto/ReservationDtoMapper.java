package com.example.solvroreservations.model.dto;

import com.example.solvroreservations.model.Reservation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Filip Wisniewski
 * Data transfer object mapper
 */
public class ReservationDtoMapper {
    private ReservationDtoMapper(){}

    public static ReservationDto mapToReservationDto(Reservation reservation) {
        return ReservationDto.builder()
                .date(reservation.getDate())
                .duration(reservation.getDuration())
                .email(reservation.getEmail())
                .fullName(reservation.getFullName())
                .numberOfSeats(reservation.getNumberOfSeats())
                .phone(reservation.getPhone())
                .tableNumber(reservation.getTableNumber())
                .build();
    }

    public static List<ReservationDto> mapToReservationDtos(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationDtoMapper::mapToReservationDto)
                .collect(Collectors.toList());
    }
}
