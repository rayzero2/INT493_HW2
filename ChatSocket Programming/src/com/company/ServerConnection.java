package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private Socket serverSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(Socket s){
        serverSocket = s;
        try {
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            out = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        String msg = null;
        try {
        while (true) {
            msg = in.readLine();
            System.out.println("Server say: "+ msg +"\n>");
        }
        }catch (Exception e){
        }
    }
}
