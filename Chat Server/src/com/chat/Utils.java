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
        catch (Exception e) {
            //remove the client
            closeClient(socket, client);
        }
        //read while finding null or end
        while (userInput != null && !userInput.endsWith("end")) {
            totalInput += userInput + '\n';
            try {
                userInput = stdIn.readLine();
            } catch (Exception e) {
                //remove the client
                closeClient(socket, client);
            }
        }

        //remove the last \n
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

                for (Client serverClient : Server.getClients()) {

                    if (serverClient.getUsername().equals(client.getUsername())) {

                        serverClient.getClientSocket().close();
                        serverClient.stopClient();
                        Server.getClients().remove(serverClient);

                        System.out.println("removed client: " + client.getUsername());

                        break;
                    }
                }
            } else {
                socket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Socket socket, String message) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //we add "\nend\n" because we want the server to see the end of the message
        //otherwise the server would wait until we send null, and that means to close the connection and we don't want that
        writer.write(message + "\nend\n");
        writer.flush();
        System.out.println("Sent message: '" + message + "'");
    }

    public static boolean isUsernameValid(String username)
    {
        //if the username is empty
        if(username.toLowerCase().trim().equals(""))
        {
            return false;
        }

        //check if the username already exists
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
