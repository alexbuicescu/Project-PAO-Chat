package com.chat;

import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static int port = 8000;

    public static void main(String[] args) {

        //Creating a SocketClient object
        Client client = null;
        scanner = new Scanner(System.in);

        try {
            String username = "";
            System.out.print("Choose a username: ");
            while((username = scanner.next()) != null)
            {
                client = new Client ("localhost", port, username);

                //if the server refused the connection, try again
                if(!client.connect(username))
                {
                    System.err.println("Username already taken!");
                    System.out.print("Choose a username: ");
                    continue;
                }
                //if the server accepted the connection, move on
                else
                {
                    break;
                }
            }
            System.out.println("Username accepted.");

            String messageToSend = "";
            //read the messages to be sent
            while((messageToSend = scanner.nextLine()) != null)
            {
                Utils.sendMessage(client.getClient(), messageToSend);
            }
        } catch (Exception e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}
