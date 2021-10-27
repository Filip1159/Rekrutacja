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
import java.util.List;

@SpringBootApplication
public class SolvroReservationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolvroReservationsApplication.class, args);
    }

    /**
     * loads list of tables into H2 in-memory database on application start
     */
    @Bean
    CommandLineRunner runner(TableRepo tableRepo) {
        return args -> {
            Resource resource = new ClassPathResource("tables.json");
            InputStream is = resource.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = bf.readLine()) != null) {
                builder.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Table[] tables = objectMapper.readValue(builder.toString(), Table[].class);
            tableRepo.saveAll(List.of(tables));
        };
    }

}
