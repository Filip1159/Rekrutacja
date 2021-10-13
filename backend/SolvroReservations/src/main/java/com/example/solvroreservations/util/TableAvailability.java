package com.example.solvroreservations.util;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TableAvailability {
    public Integer numOfSeats;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public Date availableFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public Date availableTo;
}
