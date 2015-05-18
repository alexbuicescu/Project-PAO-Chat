package com.company.SenderReceiver;


import com.company.ProducerConsumer.Producer;

/**
 * Created by Alexandru on 18-May-15.
 */
public class SenderReceiver {
    public static void main(String[] args) {

        Receiver[] receivers = new Receiver[3];

        Message message = new Message(3, receivers);

        Sender sender = new Sender(message);

        for(int i = 0; i < 3; i++)
        {
            receivers[i] = new Receiver("receiver " + i + "#", message);
            receivers[i].start();
        }

        sender.start();

    }
}
