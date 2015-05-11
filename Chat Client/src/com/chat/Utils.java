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
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        try {
            userInput = stdIn.readLine();
        }
        catch (Exception e)
        {
            if(client != null)
            {
                client.getClient().close();
            }
            else {
                socket.close();
            }
            System.exit(0);
        }
        while (userInput != null && !userInput.endsWith("end")) {
//            System.out.println("1'" + userInput + "'");
            totalInput += userInput + '\n';
            try {
                userInput = stdIn.readLine();
            }
            catch (Exception e)
            {
                if(client != null)
                {
                    client.getClient().close();
                }
                else {
                    socket.close();
                }
                System.exit(0);
            }
//            System.out.println("2'" + userInput + "'");
        }

        if(totalInput.length() > 0) {
            totalInput = totalInput.substring(0, totalInput.length() - 1);
        }

        System.out.println("Response from server: '" + totalInput + "'");
        return totalInput;
    }

    public static void sendMessage(Socket socket, String message) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(message + "\nend\n");
        writer.flush();
        System.out.println("Sent message: " + message);
    }
}
