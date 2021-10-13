package com.example.solvroreservations.controller;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.model.Table;
import com.example.solvroreservations.repo.ReservationRepo;
import com.example.solvroreservations.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ReservationRepo reservationRepo;

    @PostMapping
    public void makeReservation(@RequestBody Reservation reservation) throws IOException {
        if (reservation.getTableNumber() < 1 || reservation.getTableNumber() > 53) {
            throw new IllegalArgumentException("invalid table number: " + reservation.getTableNumber());
        }
//        if (reservation.getReservationStart().getTime() - System.currentTimeMillis() < 24*60*60*1000) {
//            throw new IllegalArgumentException("reservation should be made at least 24 hrs earlier");
//        }
        if (reservation.getReservationEnd().getTime() - reservation.getReservationStart().getTime() < 60*60*1000) {
            throw new IllegalArgumentException("reservation cannot be shorter than 1 hr");
        }
        if (reservation.getReservationEnd().getTime() - reservation.getReservationStart().getTime() > 5*60*60*1000) {
            throw new IllegalArgumentException("reservation cannot be longer than 5 hrs");
        }
        if (reservation.getReservationStart().getHours() < 11 || reservation.getReservationEnd().getHours() > 21) {
            throw new IllegalArgumentException("reservation doesn't match our working hours");
        }
        // TODO check table availability
        List<Reservation> reservationsThisDay = reservationRepo.getReservationsByTableNumberAndTime(
                reservation.getTableNumber(),
                reservation.getReservationStart(),
                reservation.getReservationEnd()
        );
        if (!reservationsThisDay.isEmpty()) {
            throw new IllegalArgumentException("Other reservations this time: " + reservationsThisDay.get(0));
        }

        Resource resource = new ClassPathResource("static/email.html");
        InputStream is = resource.getInputStream();
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String s = "", line;
        while((line = bf.readLine()) != null) {
            s += line;
        }
        s = s.replaceFirst("%RESERVATION_START%", reservation.getReservationStart().toString());
        s = s.replaceFirst("%RESERVATION_END%", reservation.getReservationEnd().toString());
        s = s.replaceFirst("%RESERVATION_ID%", "12345678");
        emailSenderService.send("cielo.armstrong50@ethereal.email", reservation.getEmail(), "Your reservation!",
                s
        );

        reservationRepo.save(reservation);
    }

    @GetMapping
    public List<Reservation> getAllReservationsThisDay() {
        return reservationRepo.findAllByDay(new Date());
    }

    @PutMapping("/{id}")
    public void cancelReservation(@PathVariable("id") Integer id) {

    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") Integer id) {

    }

}
