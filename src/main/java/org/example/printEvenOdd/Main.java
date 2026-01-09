package org.example.printEvenOdd;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        IncrementHandler iHandler = new IncrementHandler();

       Thread thread1 = new Thread(() -> {
            try {
                iHandler.printEven();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                iHandler.printOdd();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();
    }
}
