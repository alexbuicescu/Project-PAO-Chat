package com.chat;

import java.io.IOException;

public class Main {

    private static int port = 8000;

    public static void main(String[] args) {
        Server server = new Server(port);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
