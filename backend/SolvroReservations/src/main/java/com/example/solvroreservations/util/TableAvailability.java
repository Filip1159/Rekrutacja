package com.example.solvroreservations.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TableAvailability {
    public String status;
    public int minSeats;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime startDate;
    public int duration;
}
