package com.example.solvroreservations.service;

import com.example.solvroreservations.model.Reservation;
import com.example.solvroreservations.repo.ReservationRepo;
import com.example.solvroreservations.util.Mail;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Filip Wisniewski
 * Service layer class
 * Here comes user input verification and sending e-mails to clients
 */
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

    /**
     * @param reservation contains reservation details
     * @return given reservation with assigned id
     * @throws IllegalStateException forbidden user request
     * @throws IllegalArgumentException incorrect parameters specified by client
     * @throws SendFailedException internal error occurred while sending e-mail to client
     */
    public Reservation makeReservation(Reservation reservation) throws SendFailedException {
        if (reservation.getTableNumber() < 1 || reservation.getTableNumber() > 53) {
            throw new IllegalArgumentException("invalid table number: " + reservation.getTableNumber());
        }
        if (!EmailValidator.getInstance().isValid(reservation.getEmail())) {
            throw new IllegalArgumentException("invalid email address: " + reservation.getEmail());
        }
        if (!PhoneNumberUtil.getInstance().isPossibleNumber(reservation.getPhone(), "PL")) {
            throw new IllegalArgumentException("invalid phone number: " + reservation.getPhone());
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
        List<Reservation> reservationsThisTime = reservationRepo.getReservationsByTableNumberAndTime(
                reservation.getTableNumber(),
                reservation.getDate(),
                reservation.getDuration()
        );
        if (!reservationsThisTime.isEmpty()) {
            throw new IllegalStateException("other reservations this time: " + reservationsThisTime.size());
        }

        reservation.setStatus("active");
        reservation = reservationRepo.save(reservation);

        createMail.setTo(reservation.getEmail());
        createMail.setAttribute("reservationStart", reservationStart.toString());
        createMail.setAttribute("reservationEnd", reservationEnd.toString());
        createMail.setAttribute("reservationId", String.format("%06d", reservation.getId()));
        try {
            emailSenderService.send(createMail);
        } catch (MessagingException me) {
            throw new SendFailedException("something went wrong while sending email to You");
        }
        return reservation;
    }

    /**
     * @param date day which reservations come from
     * @return list of reservations that day
     */
    public List<Reservation> getAllReservationsByDay(LocalDate date) {
        return reservationRepo.findAllByDay(date);
    }

    /**
     * @param id of reservation which status will be changed
     * @param newStatus replace reservation status with this value
     * @throws IllegalStateException forbidden user request
     * @throws IllegalArgumentException incorrect parameters specified by client
     * @throws SendFailedException internal error occurred while sending e-mail to client
     */
    public void cancelReservation(int id, String newStatus) throws SendFailedException {
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
        try {
            emailSenderService.send(deleteMail);
        } catch (MessagingException me) {
            throw new SendFailedException("something went wrong while sending email to You");
        }
        reservationRepo.save(reservation);
    }

    /**
     * @param id of reservation to remove
     * @param token verificationCode user receives from put endpoint
     * @throws IllegalStateException forbidden user request
     * @throws IllegalArgumentException incorrect parameters specified by client
     */
    public void deleteReservation(int id, String token) {
        if (!reservationRepo.existsById(id)) {
            throw new IllegalArgumentException("reservation with id: " + id + " doesn't exist");
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
