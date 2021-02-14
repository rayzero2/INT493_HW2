package com.company;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ClientHandler> clients = new ArrayList<>(); //store every clients
    private static ExecutorService pool = Executors.newFixedThreadPool(5); //Thread pool for preallocate

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080), 4096);
            System.out.println("Wait for client connected");
            while (true) {
                Socket clientSocket = serverSocket.accept(); //accept
                System.out.printf("Client connected %s:%d\n",
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                ClientHandler clientThread = new ClientHandler(clientSocket, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (Exception e) {
            System.out.println("Exception in Server main");
            System.out.println(e.getStackTrace());
        }
    }
}

class  ClientHandler implements Runnable {

    private Socket clientSocket;
    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket clientScoket, ArrayList<ClientHandler> clients) {
        this.clientSocket = clientScoket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(clientSocket.getInputStream());

            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                System.out.printf("Got %s form %s:%d\n",
                        msg,
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                if(msg.equalsIgnoreCase("exit")) { //catch exit
                    clientSocket.close();
                } else if (msg.startsWith("say")){ //catch cmd say for test
                    int firstSpace = msg.indexOf(" ");
                    if (firstSpace != -1) {
                        broadcast( msg.substring(firstSpace+1));
                    }
                } else {
                    broadcast(msg);
                }
                clientSocket.getOutputStream().flush();
            }
            System.out.printf("Stream End for %s:%d\n",
                    clientSocket.getInetAddress().getHostAddress(),
                    clientSocket.getPort());
            clientSocket.close();
        } catch (Exception e){
            System.out.println("Exception in ClientHandler");
            System.out.println(e.getStackTrace());
        }
    }

    private void broadcast(String msg) {
        try {
            for (ClientHandler aClient : clients) {
                aClient.clientSocket.getOutputStream().write((msg+"\n").getBytes());
                aClient.clientSocket.getOutputStream().flush();
            }
        }catch (Exception e){
        }
    }
}
