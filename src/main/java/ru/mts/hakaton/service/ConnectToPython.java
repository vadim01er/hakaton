package ru.mts.hakaton.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ConnectToPython {

    public void connect() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(
                    new String[]{"python3", "/Users/macbook/IdeaProjects/mts/hakaton/src/main/java/ru/mts/hakaton/python/how_it_works (3).py", "packet.to"});
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = in.readLine();
            in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while (( line = in.readLine()) != null) {
                System.out.println("error is : " + line);
            }
            System.out.println("value is : " + ret);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
