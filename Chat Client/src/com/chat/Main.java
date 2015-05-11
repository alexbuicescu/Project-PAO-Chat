package com.chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    private static int port = 8000;

    public static void main(String[] args) {
	// write your code here
        //Creating a SocketClient object
        Client client;
        try {
            client = new Client ("localhost", port, "myname1");
            Scanner scanner = new Scanner(System.in);
            String messageToSend = "";
            while((messageToSend = scanner.nextLine()) != null)
            {
                Utils.sendMessage(client.getClient(), messageToSend);
            }
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}
