package com.company.SenderReceiver;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Message {

    private String message;
    private boolean ok;
    private int receivers;
    private int seenBy;

    public Message(int receivers)
    {
        this.receivers = receivers;
        seenBy = 0;
        ok = false;
    }

    public synchronized String getMessage() {

        while (!ok) {

            try {

                wait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        seenBy++;
        if(receivers == seenBy)
        {
            ok = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyAll();
        }
        return message;
    }

    public synchronized void sendMessage(String message) {

        while (ok) {

            try {

                wait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("sending: " + message + ";" + seenBy);

        ok = true;
        seenBy = 0;
        this.message = message;
        notifyAll();
    }
}
