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
        Socket clientSocket;

        while(true)
        {
            clientSocket = serverSocket.accept();

            //get the client username
            String username = Utils.readMessage(clientSocket, null).toLowerCase().trim();
            System.out.println("Client connected: '" + username + "'");

            if(Utils.isUsernameValid(username))
            {
                //accept the client
                clients.add(new Client(clientSocket, username));
                System.out.println("Client accepted: '" + username + "'");
                Utils.sendMessage(clientSocket, "1");
            }
            else
            {
                //send the client a cancel request
                System.out.println("Client not accepted: '" + username + "'");
                Utils.sendMessage(clientSocket, "0");
            }
        }

    }

    public static List<Client> getClients()
    {
        return clients;
    }
}