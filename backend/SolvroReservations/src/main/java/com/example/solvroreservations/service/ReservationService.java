package com.example.solvroreservations.service;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.repo.ReservationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final EmailSenderService emailSenderService;

    public Reservation makeReservation(Reservation reservation) throws IOException {
        if (reservation.getSeatNumber() < 1 || reservation.getSeatNumber() > 53) {
            throw new IllegalArgumentException("invalid table number: " + reservation.getSeatNumber());
        }
        LocalDateTime reservationStart = reservation.getDate();
        LocalDateTime reservationEnd = reservation.getDate().plusHours(reservation.getDuration());
        if (reservationStart.minusDays(1).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("reservation should be made at least 24 hrs earlier");
        }
        if (reservation.getDuration() < 1) {
            throw new IllegalArgumentException("reservation cannot be shorter than 1 hr");
        }
        if (reservation.getDuration() > 5) {
            throw new IllegalArgumentException("reservation cannot be longer than 5 hrs");
        }
        if (reservationStart.getHour() < 11 || reservationEnd.getHour() > 21) {
            throw new IllegalArgumentException("reservation doesn't match our working hours");
        }
        List<Reservation> reservationsThisDay = reservationRepo.getReservationsByTableNumberAndTime(
                reservation.getTableNumber(),
                reservation.getDate(),
                reservation.getDuration()
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
        s = s.replaceFirst("%RESERVATION_START%", reservationStart.toString());
        s = s.replaceFirst("%RESERVATION_END%", reservationEnd.toString());
        s = s.replaceFirst("%RESERVATION_ID%", "12345678");
        emailSenderService.send("cielo.armstrong50@ethereal.email", reservation.getEmail(), "Your reservation!",
                s
        );

        return reservationRepo.save(reservation);
    }

    public Reservation cancelReservation(int id, String newStatus) throws IOException {
        Reservation reservation = reservationRepo.getById(id);
        if (reservation.getDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reservation cannot be cancelled less than 2 hrs before its start");
        }

        Resource resource = new ClassPathResource("static/delete_token_email.html");
        InputStream is = resource.getInputStream();
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String s = "", line;
        while((line = bf.readLine()) != null) {
            s += line;
        }
        s = s.replaceFirst("%RESERVATION_START%", reservation.getDate().toString());
        s = s.replaceFirst("%RESERVATION_END%", reservation.getDate().plusHours(reservation.getDuration()).toString());
        s = s.replaceFirst("%TOKEN%", "12345678");
        emailSenderService.send("cielo.armstrong50@ethereal.email", reservation.getEmail(), "Your reservation!",
                s
        );

        reservation.setStatus(newStatus);
        return reservationRepo.save(reservation);
    }

    public List<Reservation> getAllReservationsByDay(LocalDate date) {
        return reservationRepo.findAllByDay(date);
    }

    public void deleteReservation(int id, String token) {
        Reservation reservation = reservationRepo.getById(id);
        if (reservation.getCancellationToken().equals(token) && reservation.getStatus().equals("requested cancellation")) {  // TODO token produces NullPointerExc
            reservationRepo.deleteById(id);
        }
    }
}
