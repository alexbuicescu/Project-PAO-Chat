package com.company.SenderReceiver;

import java.util.Random;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Sender extends Thread {

    private Message message;

    public Sender(Message message)
    {
        this.message = message;
    }

    public void run()
    {
        for(int i = 0; i < 10; i++)
        {
//            System.out.println("sending: " + i);
            message.sendMessage(new Random().nextInt(10) + "");
        }
    }
}
