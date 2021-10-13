package com.example.solvroreservations.service;

import com.example.solvroreservations.repo.TableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private TableRepo tableRepo;
}
