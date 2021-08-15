package ru.mts.hackathon.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ConnectToPython {

    public String connect(String value) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(
                    new String[]{"python3", "how_it_works.py", value});
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = in.readLine();
            in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("error is : " + line);
            }
            System.out.println("value is : " + ret);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not work";
    }
}
