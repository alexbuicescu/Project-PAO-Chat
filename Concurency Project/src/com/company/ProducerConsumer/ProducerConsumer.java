package com.company.ProducerConsumer;

public class ProducerConsumer {

    public static void main2(String[] args) {
//        Product product = new Product();
//        int nrOfProducts = 5;
        Product[] products = new Product[10];
        for(int i = 0; i < 10; i++)
        {
            products[i] = new Product();
        }
        new Producer(products, 10).start();
        new Consumer(products, 5).start();
    }
}
