package com.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Alexandru on 11-May-15.
 */
public class Client {

    private String hostname;
    private int port;
    private Socket client;
    private String username;

    public Client(String hostname, int port, String username){
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MessageListener().start();
    }

    public Socket getClient()
    {
        return client;
    }

    public void connect() throws UnknownHostException, IOException{
        System.out.println("Attempting to connect to "+hostname+":"+port);
        client = new Socket(hostname,port);
        client.setKeepAlive(true);
        System.out.println("Connection Established");
        Utils.sendMessage(client, username);

    }

//    public void readResponse() throws IOException{
//        String userInput;
//        BufferedReader stdIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
//
//        System.out.print("RESPONSE FROM SERVER:");
//        while ((userInput = stdIn.readLine()) != null) {
//            System.out.println(userInput);
//        }
//    }

    private class MessageListener extends Thread {
        public MessageListener() {

        }

        public void run() {
            try {
                while (true) {
                    String message = Utils.readMessage(client, Client.this);
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}