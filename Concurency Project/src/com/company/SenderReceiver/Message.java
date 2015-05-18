package com.company.SenderReceiver;

import java.util.Random;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Message {

    private String message;
    private boolean ok;
    private int receiversNumbers;
    private final Receiver[] receivers;
    private int seenBy;

    public Message(int receiversNumbers, Receiver[] receivers)
    {
        this.receiversNumbers = receiversNumbers;
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

        try {
            Thread.sleep(new Random().nextInt(10) * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        seenBy++;
        if(receiversNumbers == seenBy)
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

        System.out.println("sending: " + message);


            for (int i = 0; i < receiversNumbers; i++) {
                synchronized (receivers[i]) {
//                receivers[i].setWait();
                receivers[i].notifyAll();
            }
        }

        ok = true;
        seenBy = 0;
        this.message = message;
        notifyAll();
    }
}
