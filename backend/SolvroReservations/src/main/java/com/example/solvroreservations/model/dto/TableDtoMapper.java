package com.example.solvroreservations.model.dto;

import com.example.solvroreservations.model.Table;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Filip Wisniewski
 * Data transfer object mapper
 */
public class TableDtoMapper {
    private TableDtoMapper() {}

    public static TableDto mapToTableDto(Table table) {
        return TableDto.builder()
                .number(table.getNumber())
                .maxNumberOfSeats(table.getMaxNumberOfSeats())
                .minNumberOfSeats(table.getMinNumberOfSeats())
                .build();
    }

    public static List<TableDto> mapToTableDtos(List<Table> tables) {
        return tables.stream()
                .map(TableDtoMapper::mapToTableDto)
                .collect(Collectors.toList());
    }
}
