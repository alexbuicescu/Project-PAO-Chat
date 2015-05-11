package com.chat;

import java.io.*;
import java.net.Socket;

/**
 * Created by Alexandru on 11-May-15.
 */
public class Client {

    private Socket client;
    private String username;
    private MessageListener messageListener;

    public Client(Socket client, String username)
    {
        this.client = client;
        this.username = username;

        messageListener = new MessageListener(client, this);
        messageListener.start();
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
        messageListener.stopClient();
    }

    public MessageListener getMessageListener()
    {
        return messageListener;
    }

    public Socket getClient()
    {
        return client;
    }

}

class MessageListener extends Thread
{
    private Socket socket;
    private Client client;
    private boolean run;

    public MessageListener(Socket socket, Client client)
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

                if(!message.trim().equals("")) {
                    if (message.startsWith("$") && message.contains(":")) {

                        String sendToUsername = message.substring(1, message.indexOf(":"));
                        message = message.substring(message.indexOf(":") + 1, message.length());

                        for (Client serverClient : Server.getClients()) {
                            if (serverClient.getUsername().equals(sendToUsername)) {
                                Utils.sendMessage(serverClient.getClient(), message);
//                            serverClient.sendMessage(message);
                                return;
                            }
                        }
                        System.err.println("The user: '" + sendToUsername + "' doesn't exist");
                    } else if (message.startsWith("#rename:")) {
                        String oldUsername = client.getUsername();
                        client.setUsername(message.substring(message.indexOf(":") + 1, message.length()));
                        System.out.println("Renamed '" + oldUsername + "' to: '" + client.getUsername() + "'");
                    } else if (message.startsWith("#showall:")) {
                        for(Client serverClient : Server.getClients())
                        {
                            System.out.println("client: " + serverClient.getUsername());
                        }
                    } else {
                        if (message.startsWith("#all:")) {
                            message = message.substring(5, message.length());
                        }
                        for (Client serverClient : Server.getClients()) {
                            //if it isn't the current user
                            if (!client.getUsername().equals(serverClient.getUsername())) {
                                Utils.sendMessage(serverClient.getClient(), message);
//                            serverClient.sendMessage(message);
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
