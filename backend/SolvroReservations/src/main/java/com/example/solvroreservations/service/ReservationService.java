package com.example.solvroreservations.service;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.repo.ReservationRepo;
import com.example.solvroreservations.util.Mail;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final EmailSenderService emailSenderService;
    private final Mail deleteMail;
    private final Mail createMail;

    public ReservationService(ReservationRepo reservationRepo, EmailSenderService emailSenderService,
                              Mail deleteMail, Mail createMail) {
        this.reservationRepo = reservationRepo;
        this.emailSenderService = emailSenderService;
        this.deleteMail = deleteMail;
        this.createMail = createMail;
    }

    public Reservation makeReservation(Reservation reservation) throws MessagingException {
        if (reservation.getSeatNumber() < 1 || reservation.getSeatNumber() > 53) {
            throw new IllegalArgumentException("invalid table number: " + reservation.getSeatNumber());
        }
        LocalDateTime reservationStart = reservation.getDate();
        LocalDateTime reservationEnd = reservation.getDate().plusHours(reservation.getDuration());
        if (reservationStart.minusDays(1).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("reservation should be made at least 24 hrs earlier");
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
            throw new IllegalStateException("Other reservations this time: " + reservationsThisDay.get(0));
        }

        reservation.setStatus("active");
        reservation = reservationRepo.save(reservation);

        createMail.setTo(reservation.getEmail());
        createMail.setAttribute("reservationStart", reservationStart.toString());
        createMail.setAttribute("reservationEnd", reservationEnd.toString());
        createMail.setAttribute("reservationId", String.format("%06d", reservation.getId()));
        emailSenderService.sendMessageUsingThymeleafTemplate(createMail);

        return reservation;
    }

    public List<Reservation> getAllReservationsByDay(LocalDate date) {
        return reservationRepo.findAllByDay(date);
    }

    public void cancelReservation(int id, String newStatus) throws MessagingException {
        if (!reservationRepo.existsById(id)) {
            throw new EntityNotFoundException("reservation with id: " + id + " doesn't exist");
        }
        Reservation reservation = reservationRepo.getById(id);
        if (reservation.getDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reservation cannot be cancelled less than 2 hrs before its start");
        }
        if (!newStatus.equals("requested cancellation")) {
            throw new IllegalArgumentException("newStatus best be equal to requested cancellation");
        }

        reservation.setStatus(newStatus);
        reservation.setCancellationToken(String.format("%06d", (int) (Math.random() * 1000000)));

        deleteMail.setTo(reservation.getEmail());
        deleteMail.setAttribute("reservationFullName", reservation.getFullName());
        deleteMail.setAttribute("reservationStart", reservation.getDate().toString());
        deleteMail.setAttribute("reservationEnd",
                reservation.getDate().plusHours(reservation.getDuration()).toString());
        deleteMail.setAttribute("token", reservation.getCancellationToken());
        emailSenderService.sendMessageUsingThymeleafTemplate(deleteMail);

        reservationRepo.save(reservation);
    }

    public void deleteReservation(int id, String token) {
        if (!reservationRepo.existsById(id)) {
            throw new EntityNotFoundException("reservation with id: " + id + " doesn't exist");
        }
        Reservation reservation = reservationRepo.getById(id);
        if (token == null) {
            throw new IllegalArgumentException("cancellation token is null");
        }
        if (!reservation.getCancellationToken().equals(token)) {
            throw new IllegalArgumentException("incorrect cancellation token");
        }
        if (!reservation.getStatus().equals("requested cancellation")) {
            throw new IllegalStateException("generate your token before deleting");
        }
        reservationRepo.deleteById(id);
    }
}
