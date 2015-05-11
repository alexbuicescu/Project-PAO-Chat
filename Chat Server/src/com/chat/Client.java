package com.chat;

import java.io.*;
import java.net.Socket;

/**
 * Created by Alexandru on 11-May-15.
 */
public class Client {

    private Socket clientSocket;
    private String username;
    private MessageListenerThread messageListenerThread;

    public Client(Socket clientSocket, String username)
    {
        this.clientSocket = clientSocket;
        this.username = username;

        messageListenerThread = new MessageListenerThread(clientSocket, this);
        messageListenerThread.start();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        username = username.toLowerCase().trim();

        if(Utils.isUsernameValid(username));
        {
            this.username = username;
        }
    }

    public void stopClient()
    {
        messageListenerThread.stopClient();
    }

    public MessageListenerThread getMessageListenerThread()
    {
        return messageListenerThread;
    }

    public Socket getClientSocket()
    {
        return clientSocket;
    }

}

class MessageListenerThread extends Thread
{
    private Socket socket;
    private Client client;
    private boolean run;

    public MessageListenerThread(Socket socket, Client client)
    {
        this.socket = socket;
        this.client = client;
        run = true;
    }

    public void stopClient()
    {
        run = false;
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try {
            while(run) {
                String message = Utils.readMessage(socket, client);

                //if the message is not empty
                if(!message.trim().equals("")) {

                    //send message to a specific user
                    if (message.startsWith("$") && message.contains(":")) {

                        //get the user
                        String sendToUsername = message.substring(
                                1, message.indexOf(":")
                        );
                        //get the message
                        message = message.substring(
                                message.indexOf(":") + 1, message.length()
                        );

                        //find the user
                        for (Client serverClient : Server.getClients()) {

                            if (serverClient.getUsername().equals(sendToUsername)) {

                                Utils.sendMessage(
                                        serverClient.getClientSocket(), message
                                );
                                return;
                            }
                        }
                        //didn't find the user
                        System.err.println("The user: '" + sendToUsername + "' doesn't exist");

                    }
                    //rename a user
                    else if (message.startsWith("#rename:")) {

                        String oldUsername = client.getUsername();
                        //set new username
                        client.setUsername(
                                message.substring(
                                        message.indexOf(":") + 1, message.length()
                                ));
                        System.out.println("Renamed '" + oldUsername + "' to: '" + client.getUsername() + "'");

                    }
                    //show all the users
                    else if (message.startsWith("#showall:")) {

                        for(Client serverClient : Server.getClients())
                        {
                            System.out.println("clientSocket: " + serverClient.getUsername());
                        }
                    }
                    //send broadcast message
                    else {

                        //if it starts with #all: then remove it
                        if (message.startsWith("#all:")) {

                            message = message.substring(
                                    message.indexOf(":") + 1, message.length()
                            );
                        }

                        //send the message to every user
                        for (Client serverClient : Server.getClients()) {

                            //if it isn't the current user (you don't want to send a message to yourself, don't you?)
                            if (!client.getUsername().equals(serverClient.getUsername())) {

                                Utils.sendMessage(
                                        serverClient.getClientSocket(), message
                                );
                            }
                        }

                        System.out.println("Sent to everybody:" + message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
