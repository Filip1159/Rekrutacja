package com.example.solvroreservations.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Filip Wisniewski
 * Model class for client request about tables available in specified period in time
 */
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TableAvailability {
    public String status;
    public int minSeats;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    public LocalDateTime startDate;
    public int duration;
}
