package com.example.solvroreservations.repo;

import com.example.solvroreservations.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TableRepo extends JpaRepository<Table, Integer> {

    @Query(
            value = "SELECT * FROM tables " +
                    "WHERE min_number_of_seats <= ?1 AND ?1 <= max_number_of_seats " +
                    "AND number NOT IN (" +
                        "SELECT tables.number FROM tables, reservations " +
                        "WHERE tables.number = reservations.table_number " +
                        "AND ((reservation_start < ?2 AND ?2 < reservation_end) OR (reservation_start < ?3 AND ?3 < reservation_end))" +
                    ")",
            nativeQuery = true
    )
    List<Table> getFreeTables(Integer numOfSeats, Date availableFrom, Date availableTo);
}
