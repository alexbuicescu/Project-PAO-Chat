package com.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru on 11-May-15.
 */
public class Server {

    private ServerSocket serverSocket;
    private int port;
    private static List<Client> clients = new ArrayList<Client>();

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);

        //Listen for clients. Block till one connects

        System.out.println("Waiting for clients...");
        Socket client;// = serverSocket.accept();

        while(true)
        {
            client = serverSocket.accept();

            String username = Utils.readMessage(client, null).toLowerCase().trim();
            System.out.println("Client connected: '" + username + "'");
            boolean usernameValid = true;

            for(Client serverClient : clients)
            {
                if(serverClient.getUsername().equals(username))
                {
                    usernameValid = false;
                    break;
                }
            }

            if(username.equals(""))
            {
                usernameValid = false;
            }

            if(usernameValid)
            {
                clients.add(new Client(client, username));
                System.out.println("Client accepted: '" + username + "'");
                Utils.sendMessage(client, "1");
            }
            else
            {
                System.out.println("Client not accepted: '" + username + "'");
                Utils.sendMessage(client, "0");
            }
        }

    }

    public static List<Client> getClients()
    {
        return clients;
    }
}