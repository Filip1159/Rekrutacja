package com.example.solvroreservations.controller;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.model.dto.ReservationDto;
import com.example.solvroreservations.model.dto.ReservationDtoMapper;
import com.example.solvroreservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationDto makeReservation(@RequestBody Reservation reservation) throws IOException {
        return ReservationDtoMapper.mapToReservationDto(reservationService.makeReservation(reservation));
    }

    @GetMapping
    public List<ReservationDto> getAllReservationsByDay(@RequestBody Map<String, LocalDate> query) {
        return ReservationDtoMapper.mapToReservationDtos(reservationService.getAllReservationsByDay(query.get("date")));
    }

    @PutMapping("/{id}")
    public ReservationDto cancelReservation(@PathVariable("id") int id, @RequestBody Map<String, String> query) throws IOException {
        return ReservationDtoMapper.mapToReservationDto(reservationService.cancelReservation(id, query.get("status")));
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") int id, @RequestBody Map<String, String> query) {
        reservationService.deleteReservation(id, query.get("verificationCode"));
    }

}
