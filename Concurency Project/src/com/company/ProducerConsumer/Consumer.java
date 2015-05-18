package com.company.ProducerConsumer;

import java.util.Random;

/**
 * Created by Alexandru on 18-May-15.
 */
public class Consumer extends Thread {

    private Product[] products;
    private int nrOfProducts;

    public Consumer(Product[] products, int nrOfProducts) {
        this.products = products;
        this.nrOfProducts = nrOfProducts;
    }

    public void run() {

        for (int i = 0; i < nrOfProducts; i++) {

            System.out.println("Consumer: " + products[i].getInfo());


            try {

                sleep((int) (new Random().nextInt(50) * 100));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
