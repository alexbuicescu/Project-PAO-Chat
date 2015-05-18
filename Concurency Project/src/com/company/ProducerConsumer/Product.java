package com.company.ProducerConsumer;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Product {

    private int info;
    private boolean ok = false;

    public synchronized int getInfo() {

        while (!ok) {

            try {

                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ok = false;
        notifyAll();
        return info;
    }

    public synchronized void setInfo(int info) {

        while (ok) {

            try {

                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ok = true;
        this.info = info;
        notifyAll();
    }
}

