package com.example.solvroreservations.controller;

import com.example.solvroreservations.model.dto.TableDto;
import com.example.solvroreservations.model.dto.TableDtoMapper;
import com.example.solvroreservations.service.TableService;
import com.example.solvroreservations.util.TableAvailability;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @GetMapping
    public List<TableDto> getFreeTables(@RequestBody TableAvailability tableAvailability) {
        System.out.println(tableAvailability + "from controller");
        return TableDtoMapper.mapToTableDtos(
                tableService.getFreeTables(tableAvailability)
        );
    }
}
