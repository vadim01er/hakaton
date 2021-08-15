package ru.mts.hackathon.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mts.hackathon.service.ConnectToPython;

@Controller
public class ApiController {

    @GetMapping("/")
    public ResponseEntity getStatus() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/check")
    public ResponseEntity<String> getCheckMapping(@RequestParam("value") String value) {
        ConnectToPython python = new ConnectToPython();
        String connect = python.connect(value);
        return ResponseEntity.ok(connect);
    }

    @GetMapping("/main")
    public String index(@RequestParam(value = "value", required = false) String value, Model model) {
        ConnectToPython python = new ConnectToPython();
        String connect;
        if (value != null) {
            connect = python.connect(value);
            model.addAttribute("result", connect);
        }
        model.addAttribute("value", value);
        return "main";
    }
}
