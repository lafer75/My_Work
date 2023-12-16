package com.kaluzny.demo.web;

import com.kaluzny.demo.domain.Automobile;
import com.kaluzny.demo.domain.AutomobileRepository;
import jakarta.jms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AutomobilePublisher {

    private final JmsTemplate jmsTemplate;
    private final AutomobileRepository repository;

    public AutomobilePublisher(JmsTemplate jmsTemplate, AutomobileRepository repository) {
        this.jmsTemplate = jmsTemplate;
        this.repository = repository;
    }

    public ResponseEntity<List<Automobile>> findCarsByColor(String color) {
        List<Automobile> allAutomobiles = repository.findAll();
        List<Automobile> filteredCars = new ArrayList<>();

        for (Automobile automobile : allAutomobiles) {
            if (color == null || color.isEmpty() || color.equalsIgnoreCase(automobile.getColor())) {
                jmsTemplate.convertAndSend("AutoTopic", automobile);
                filteredCars.add(automobile);
            }
        }

        if (!filteredCars.isEmpty()) {
            return ResponseEntity.ok(filteredCars);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    protected void publishAutomobileToTopic(Automobile automobile) throws Exception {
        try {
            Topic autoTopic = Objects.requireNonNull(jmsTemplate.getConnectionFactory())
                    .createConnection()
                    .createSession()
                    .createTopic("AutoTopic");
            log.info("\u001B[32m" + "Sending Automobile with id: " + automobile.getId() + "\u001B[0m");
            jmsTemplate.convertAndSend(autoTopic, automobile);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
