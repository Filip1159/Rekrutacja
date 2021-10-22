package com.example.solvroreservations.controller;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.model.dto.ReservationDto;
import com.example.solvroreservations.model.dto.ReservationDtoMapper;
import com.example.solvroreservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Map<String, String>> makeReservation(@RequestBody Reservation reservation) throws IOException, MessagingException {
        try {
            Reservation savedReservation = reservationService.makeReservation(reservation);
            HashMap<String, String> response = new HashMap<>();
            response.put("reservationId", String.format("%06d", savedReservation.getId()));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (IllegalStateException ise) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservationsByDay(@RequestBody Map<String, LocalDate> query) {
        return ResponseEntity.ok(
                ReservationDtoMapper.mapToReservationDtos(reservationService.getAllReservationsByDay(query.get("date")))
        );
    }

    @PutMapping("/{id}")
    public void cancelReservation(@PathVariable("id") int id, @RequestBody Map<String, String> query) throws IOException, MessagingException {
        try {
            reservationService.cancelReservation(id, query.get("status"));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (IllegalStateException ise) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getMessage());
        } catch (EntityNotFoundException enfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enfe.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") int id, @RequestBody Map<String, String> query) {
        try {
            reservationService.deleteReservation(id, query.get("verificationCode"));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (IllegalStateException ise) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getMessage());
        } catch (EntityNotFoundException enfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enfe.getMessage());
        }
    }

}
