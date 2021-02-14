package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable {
    private Socket serverSocket;
    private BufferedReader in;

    public ServerConnection(Socket s){
        serverSocket = s;
        try {
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Exception in ServerConnection");
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        String msg = null;
        try {
        while (true) {
            msg = in.readLine();
            System.out.printf("Server say: %s \n>", msg );
        }
        }catch (Exception e){
            System.out.println("Exception in @run ServerConnection");
            System.out.println(e.getStackTrace());
        }
    }
}
