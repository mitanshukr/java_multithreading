package org.example.printEvenOdd;

public class IncrementHandler {

    volatile int count = 0;

    public synchronized void printEven() throws InterruptedException {
        while (count <= 20) {
            if (count % 2 == 0) {
                System.out.println(count++);
                notify();
            } else {
                wait();
            }
        }
    }

    public synchronized void printOdd() throws InterruptedException {
        while (count <= 20) {
            if (count % 2 != 0) {
                System.out.println(count++);
                notify();
            } else {
                wait();
            }
        }
    }
}