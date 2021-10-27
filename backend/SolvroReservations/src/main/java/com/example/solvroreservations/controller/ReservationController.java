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

import javax.mail.SendFailedException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Filip Wisniewski
 * Client interaction layer class
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * @param reservation contains information about client's requested reservation
     * @return unique reservation id, that also will be sent in e-mail
     * or server error details if reservation cannot be saved successfully
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> makeReservation(@RequestBody Reservation reservation) {
        try {
            Reservation savedReservation = reservationService.makeReservation(reservation);
            HashMap<String, String> response = new HashMap<>();
            response.put("reservationId", String.format("%06d", savedReservation.getId()));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (IllegalStateException ise) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getMessage());
        } catch (SendFailedException sfe) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, sfe.getMessage());
        }
    }

    /**
     * @param query contains one single property: date in ISO 8601 format
     * @return list of reservations that day
     */
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservationsByDay(@RequestBody Map<String, LocalDate> query) {
        return ResponseEntity.ok(
                ReservationDtoMapper.mapToReservationDtos(reservationService.getAllReservationsByDay(query.get("date")))
        );
    }

    /**
     * @param id unique reservation id that should be affected
     * @param query contains one single property: new reservation status
     */
    @PutMapping("/{id}")
    public void cancelReservation(@PathVariable("id") int id, @RequestBody Map<String, String> query) {
        try {
            reservationService.cancelReservation(id, query.get("status"));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
        } catch (IllegalStateException ise) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ise.getMessage());
        } catch (EntityNotFoundException enf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, enf.getMessage());
        } catch (SendFailedException sfe) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, sfe.getMessage());
        }
    }

    /**
     * @param id unique reservation id that should be affected
     * @param query contains one single property: verification code send via e-mail after calling put endpoint
     */
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
