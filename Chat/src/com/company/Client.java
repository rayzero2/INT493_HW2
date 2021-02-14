package com.company;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        Scanner scIn1 = new Scanner(System.in);
        System.out.print("Please input IP : ");
        String IP = scIn1.nextLine();
        Socket clientSocket = new Socket();
        System.out.println("Connecting");
        clientSocket.connect(new InetSocketAddress(IP, 8080));
        System.out.printf("connected form port %d\n",
                clientSocket.getLocalPort());
        ServerConnection serverCon = new ServerConnection(clientSocket);

        Scanner scIn2 = new Scanner(System.in);

        new Thread(serverCon).start();

        while (true) {

            System.out.print(">");
            String mess = scIn2.nextLine();
            if (mess.equalsIgnoreCase("exit")) {
                String data = mess + "\n";
                clientSocket.getOutputStream().write(data.getBytes());
                clientSocket.getOutputStream().flush();
                break;
            }
            String data = mess + "\n";
            clientSocket.getOutputStream().write(data.getBytes());
            clientSocket.getOutputStream().flush();
//            ===================move to serverConnection==================
//                Scanner sc = new Scanner(clientSocket.getInputStream());
//                String message = sc.nextLine();
//                System.out.println("From sv : " + message);
//            }
//        else { //Catch other cmd don't use anymore
//                System.out.println("Wrong command, Nice Try");
//            }
//            =============================================================
            }
        clientSocket.close();
    }
}
