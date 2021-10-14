package com.example.solvroreservations.service;

import com.example.solvroreservations.model.Table;
import com.example.solvroreservations.repo.TableRepo;
import com.example.solvroreservations.util.TableAvailability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepo tableRepo;

    public List<Table> getFreeTables(TableAvailability tableAvailability) {
        if (!tableAvailability.status.equals("active")) {
            throw new IllegalArgumentException("you should set status criterion to active");
        }
        if (tableAvailability.minSeats < 1) {
            throw new IllegalArgumentException("minSeats must be positive");
        }
        if (tableAvailability.duration < 1) {
            throw new IllegalArgumentException("duration must be at least 1 hr");
        }
        LocalDateTime reservationEnd = tableAvailability.startDate.plusHours(tableAvailability.duration);
        if (tableAvailability.startDate.getHour() < 11 ||
                reservationEnd.isAfter(LocalDateTime.of(tableAvailability.startDate.toLocalDate(), LocalTime.of(21, 0))))
        {
            throw new IllegalArgumentException("date doesn't match our working hours");
        }

        List<Table> result = tableRepo.getFreeTables(tableAvailability.minSeats, tableAvailability.startDate, tableAvailability.duration);

        System.out.println(result + " from service");
        return result;
    }
}
