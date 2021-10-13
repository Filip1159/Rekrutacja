package com.example.solvroreservations.repo;

import com.example.solvroreservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    // IT WORKS, DON'T TOUCH
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE table_number = ?1 " +
                    "AND ((reservation_start < ?2 AND ?2 < reservation_end) OR (reservation_start < ?3 AND ?3 < reservation_end))",
            nativeQuery = true
    )
    List<Reservation> getReservationsByTableNumberAndTime(Integer tableNumber, Date reservationStart, Date reservationEnd);

    // IT WORKS, DON'T TOUCH
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE DAY_OF_YEAR(?1) = DAY_OF_YEAR(reservation_start) AND YEAR(?1) = YEAR(reservation_start)",
            nativeQuery = true
    )
    List<Reservation> findAllByDay(Date date);
}
