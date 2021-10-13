package com.example.solvroreservations.controller;

import com.example.solvroreservations.model.Table;
import com.example.solvroreservations.repo.TableRepo;
import com.example.solvroreservations.util.TableAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private TableRepo tableRepo;

    @GetMapping
    public List<Table> getFreeTables(@RequestBody TableAvailability tableAvailability) {
        return tableRepo.getFreeTables(tableAvailability.numOfSeats, tableAvailability.availableFrom, tableAvailability.availableTo);
    }
}
