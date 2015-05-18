package com.company.ProducerConsumer;

import java.util.Random;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Producer extends Thread {

    private Product[] products;
    private int nrOfProducts;

    public Producer(Product[] products, int nrOfProducts) {
        this.products = products;
        this.nrOfProducts = nrOfProducts;
    }

    public void run() {

        for (int i = 0; i < nrOfProducts; i++) {

            products[i].setInfo(i);

            System.out.println("Producer: " + i);

            try {

                sleep((int) (new Random().nextInt(10) * 100));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
