package com.chat;

import java.io.*;
import java.net.Socket;

/**
 * Created by Alexandru on 11-May-15.
 */
public class Utils {

    public static String readMessage(Socket socket, Client client) throws IOException {
        String userInput = null;
        String totalInput = "";
        BufferedReader stdIn = null;

        try {
            stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = stdIn.readLine();
        }
        catch (Exception e)
        {
            //remove the client
            closeClient(socket, client);
        }
        while (userInput != null && !userInput.endsWith("end")) {
//            System.out.println("1'" + userInput + "'");
            totalInput += userInput + '\n';
            try {
                userInput = stdIn.readLine();
            }
            catch (Exception e)
            {
                //remove the client
                closeClient(socket, client);
            }
//            System.out.println("2'" + userInput + "'");
        }

        if(totalInput.length() > 0) {
            totalInput = totalInput.substring(0, totalInput.length() - 1);
        }

        System.out.println("Response from server: '" + totalInput + "'");
        return totalInput;
    }

    private static void closeClient(Socket socket, Client client)
    {
        System.out.println("Closing client");
        try {
            if (client != null) {
                System.out.println("Closing client: " + client.getUsername());

                for (Client serverClient : Server.getClients()) {

                    if (serverClient.getUsername().equals(client.getUsername())) {
                        System.out.println("client: '" + client.getUsername() + "' '" + serverClient.getUsername() + "'");
                        serverClient.getClient().close();
                        serverClient.stopClient();
//                        serverClient.getMessageListener().join();
                        Server.getClients().remove(serverClient);
                        System.out.println("removed client: " + client.getUsername());
                        break;
                    }
                    else
                    {
                        System.out.println("not client: '" + client.getUsername() + "' '" + serverClient.getUsername() + "'");
                    }
                }
            } else {
                socket.close();
            }
        }
        catch (Exception e)
        {

        }
    }

    public static void sendMessage(Socket socket, String message) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(message + "\nend\n");
        writer.flush();
        System.out.println("Sent message: '" + message + "'");
    }

    public static boolean isUsernameValid(String username)
    {
        if(username.toLowerCase().trim().equals(""))
        {
            return false;
        }

        for(Client serverClient : Server.getClients())
        {
            if(serverClient.getUsername().equals(username.toLowerCase().trim()))
            {
                return false;
            }
        }

        return true;
    }
}
