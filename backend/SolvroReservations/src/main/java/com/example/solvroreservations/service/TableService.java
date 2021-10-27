package com.example.solvroreservations.service;

import com.example.solvroreservations.model.Table;
import com.example.solvroreservations.repo.TableRepo;
import com.example.solvroreservations.util.TableAvailability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Filip Wisniewski
 * Service layer class responsible for client input verification
 */
@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepo tableRepo;

    /**
     * @param tableAvailability contains details about tables that are supposed to be available in specified period in time
     * @return list of free tables that satisfies given criteria
     */
    public List<Table> getFreeTables(TableAvailability tableAvailability) {
        if (!tableAvailability.status.equals("active")) {
            throw new IllegalArgumentException("you should set status criterion to active");
        }
        if (tableAvailability.minSeats < 1) {
            throw new IllegalArgumentException("minSeats must be positive");
        }
        if (tableAvailability.duration < 1) {
            throw new IllegalArgumentException("duration must be at least 1 hr long");
        }
        LocalDateTime reservationEnd = tableAvailability.startDate.plusHours(tableAvailability.duration);
        if (tableAvailability.startDate.getHour() < 11 ||
                reservationEnd.isAfter(LocalDateTime.of(tableAvailability.startDate.toLocalDate(), LocalTime.of(21, 0))))
        {
            throw new IllegalArgumentException("date doesn't match our working hours");
        }
        return tableRepo.getFreeTables(tableAvailability.minSeats, tableAvailability.startDate, tableAvailability.duration);
    }
}
