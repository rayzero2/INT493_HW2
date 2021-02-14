package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection implements Runnable {
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(Socket s){
        server = s;
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message = null;
        try {
        while (true) {
            message = in.readLine();
            System.out.println("Server say: " + message);
        }
        }catch (Exception e){

        }
    }
}
