package ru.mts.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mts.hackathon.service.ConnectToPython;

@SpringBootApplication
public class HackathonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackathonApplication.class, args);
        ConnectToPython python = new ConnectToPython();
        python.connect("po");
    }

}
