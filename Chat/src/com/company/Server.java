package com.company;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080), 4096);
            System.out.println("Wait for client connected");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("Client connected %s:%d\n",
                        clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                ClientHandler clientThread = new ClientHandler(clientSocket, clients);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                String mess = sc.nextLine();
                System.out.printf("Got %s form %s:%d\n", mess, clientSocket.getInetAddress().getHostAddress(),
                        clientSocket.getPort());
                String data;
//                if(mess.equalsIgnoreCase("exit")) { //catch exit
//                    data = String.format("Logout TIME:%d\n",
//                            System.currentTimeMillis());
//                    clientSocket.getOutputStream().write(data.getBytes());
//                    clientSocket.close();
//                } else
                if (mess.startsWith("say")){
                    int firstSpace = mess.indexOf(" ");
                    if (firstSpace != -1) {
                        broadcast( mess.substring(firstSpace+1));
                    }
                } else {

                    clientSocket.getOutputStream().write((mess+"\n").getBytes());
                }
                clientSocket.getOutputStream().flush();
            }
//        read/write to clientSocket
            System.out.printf("Stream End for %s:%d\n",clientSocket.getInetAddress().getHostAddress(),
                    clientSocket.getPort());
            clientSocket.close();
        } catch (Exception e){
            //do nothing
        }
    }



    private void broadcast(String mess) {
        try {
            for (ClientHandler aClient : clients) {
                aClient.clientSocket.getOutputStream().write((mess+"\n").getBytes());
                aClient.clientSocket.getOutputStream().flush();
            }
        }catch (Exception e){

        }
    }
}
