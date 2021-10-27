package com.example.solvroreservations.repo;

import com.example.solvroreservations.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Filip Wisniewski
 * Data access object
 * queries written in H2 dialect
 * @see <a href="http://www.h2database.com/html/grammar.html">...</a>
 */
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    /**
     * @param tableNumber that is supposed to be available in given period of time
     * @param reservationStart beginning of period in time when table is supposed to be available
     * @param duration in hours when table is supposed be available
     * @return list of reservation that overlap with given period of time
     */
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE table_number = ?1 " +
                    "AND ((date < ?2 AND ?2 < DATEADD('HOUR', duration, date)) OR (date < DATEADD('HOUR', ?3, ?2) AND DATEADD('HOUR', ?3, ?2)  < DATEADD('HOUR', duration, date)))",
            nativeQuery = true
    )
    List<Reservation> getReservationsByTableNumberAndTime(Integer tableNumber, LocalDateTime reservationStart, int duration);

    /**
     * @param date when reservations will be obtained
     * @return list of all reservations that day
     */
    @Query(
            value = "SELECT * FROM reservations " +
                    "WHERE DAY_OF_YEAR(?1) = DAY_OF_YEAR(date) AND YEAR(?1) = YEAR(date)",
            nativeQuery = true
    )
    List<Reservation> findAllByDay(LocalDate date);
}
