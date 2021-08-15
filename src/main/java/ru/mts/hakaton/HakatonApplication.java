package ru.mts.hakaton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mts.hakaton.service.ConnectToPython;

//@SpringBootApplication
public class HakatonApplication {

    public static void main(String[] args) {
//        SpringApplication.run(HakatonApplication.class, args);
        ConnectToPython python = new ConnectToPython();
        python.connect();
    }

}
