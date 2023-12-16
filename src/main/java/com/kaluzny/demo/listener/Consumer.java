package com.kaluzny.demo.listener;

import com.kaluzny.demo.domain.Automobile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Consumer {

    @JmsListener(destination = "AutoTopic", containerFactory = "automobileJmsContFactory")
    public void getAutomobileListener1(Automobile automobile) {
        if (automobile.getColor().equalsIgnoreCase("Red")) {
            log.info("\u001B[31m" + "Automobile Consumer 1: " + automobile + "\u001B[0m");
        } else {
            log.info("\u001B[0m" + "Not a red car "  + automobile + "\u001B[0m");
        }
    }

    @JmsListener(destination = "AutoTopic", containerFactory = "automobileJmsContFactory")
    public void getAutomobileListener2(Automobile automobile) {
        if (automobile.getColor().equalsIgnoreCase("Blue")) {
            log.info("\u001B[34m" + "Automobile Consumer 2: " + automobile + "\u001B[0m");
        } else {
            log.info("\u001B[0m" + "Not a blue car "  + automobile + "\u001B[0m");
        }
    }

    @JmsListener(destination = "AutoTopic", containerFactory = "automobileJmsContFactory")
    public void getAutomobileListener3(Automobile automobile) {
        if (automobile.getColor().equalsIgnoreCase("Green")) {
            log.info("\u001B[32m" + "Automobile Consumer 3: " + automobile + "\u001B[0m");
        } else {
            log.info("\u001B[0m" + "Not a green car "  + automobile + "\u001B[0m");
        }
    }
}
