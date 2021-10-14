package com.example.solvroreservations.repo;

import com.example.solvroreservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    // IT WORKS, DON'T TOUCH
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE table_number = ?1 " +
                    "AND ((date < ?2 AND ?2 < DATEADD('HOUR', duration, date)) OR (date < DATEADD('HOUR', ?3, ?2) AND DATEADD('HOUR', ?3, ?2)  < DATEADD('HOUR', duration, date)))",
            nativeQuery = true
    )
    List<Reservation> getReservationsByTableNumberAndTime(Integer tableNumber, LocalDateTime reservationStart, int duration);

    // IT WORKS, DON'T TOUCH
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE DAY_OF_YEAR(?1) = DAY_OF_YEAR(date) AND YEAR(?1) = YEAR(date)",
            nativeQuery = true
    )
    List<Reservation> findAllByDay(LocalDate date);
}
