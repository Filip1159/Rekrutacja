package com.example.solvroreservations.repo;

import com.example.solvroreservations.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Filip Wisniewski
 * Data access object
 * queries written in H2 dialect
 * @see <a href="http://www.h2database.com/html/grammar.html">...</a>
 */
public interface TableRepo extends JpaRepository<Table, Integer> {

    /**
     * @param numOfSeats that table is able to have
     * @param date beginning of the period of time, when table is supposed to be free
     * @param duration in hours of table being available
     * @return list of tables satisfying specified criteria
     */
    @Query(
            value = "SELECT * FROM tables " +
                    "WHERE min_number_of_seats <= ?1 AND ?1 <= max_number_of_seats " +
                    "AND number NOT IN (" +
                        "SELECT tables.number FROM tables, reservations " +
                        "WHERE tables.number = reservations.seat_number " +
                        "AND (" +
                            "(reservations.date < ?2 AND ?2 < DATEADD('HOUR', reservations.duration, reservations.date))" +
                            "OR (reservations.date < DATEADD('HOUR', ?3, ?2) AND DATEADD('HOUR', ?3, ?2) < DATEADD('HOUR', reservations.duration, reservations.date))" +
                        ")" +
                    ")",
            nativeQuery = true
    )
    List<Table> getFreeTables(Integer numOfSeats, LocalDateTime date, int duration);
}
