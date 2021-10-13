package com.example.solvroreservations;

import com.example.solvroreservations.model.Table;
import com.example.solvroreservations.repo.TableRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SolvroReservationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolvroReservationsApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(TableRepo tableRepo) {
        return args -> {
            Resource resource = new ClassPathResource("tables.json");
            InputStream is = resource.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String s = "", line;
            while((line = bf.readLine()) != null) {
                s += line;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Table[] tables = objectMapper.readValue(s, Table[].class);

            for ( Table t : tables ) {
                System.out.println(t);
            }

            tableRepo.saveAll(List.of(tables));
        };
    }

}
