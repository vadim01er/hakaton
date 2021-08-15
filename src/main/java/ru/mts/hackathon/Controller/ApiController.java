package ru.mts.hackathon.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApiController {

    @GetMapping("/")
    public ResponseEntity getStatus() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/check")
    public ResponseEntity<String> getCheckMapping(@RequestParam("value") String value) {

        return ResponseEntity.ok("");
    }
}
