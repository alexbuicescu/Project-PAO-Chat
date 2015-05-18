package com.company.SenderReceiver;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Receiver extends Thread {

    private Message message;
    private String name;

    public Receiver(String name, Message message)
    {
        this.name = name;
        this.message = message;
    }

    public synchronized void run()
    {
        while(true)
        {
            try {

                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(name + ": " + message.getMessage());
        }
    }
}
