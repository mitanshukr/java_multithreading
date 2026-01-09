package org.example.semaphore.producerConsumer;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {
    public static void main(String[] args) {

        ProducerConsumerMulti prodCons = new ProducerConsumerMulti(5);

        Thread producerThread = new Thread(() -> {
            try {
                prodCons.producer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread consumerThread = new Thread(() -> {
            try {
                prodCons.consumer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producerThread.start();
        consumerThread.start();

    }
}

class ProducerConsumerMulti {
    int capacity;
    Semaphore full = new Semaphore(0);
    Semaphore empty;
    Queue<Item> queue = new ArrayDeque<>();
    Lock lock = new ReentrantLock();

    public ProducerConsumerMulti(int capacity){
        this.capacity = capacity;
        this.empty = new Semaphore(capacity);
    }

    public void producer() throws InterruptedException {
        while (true) {
            empty.acquire();
            System.out.println("--- producing message ----");
//            Thread.sleep(1000);
            lock.lock();
           queue.offer(produceNewItem());
            lock.unlock();
            full.release();
        }
    }

    public void consumer() throws InterruptedException {
        while (true) {
            full.acquire();
            System.out.println("#### consuming message #####");
            Thread.sleep(1000);
            lock.lock();
            System.out.println(queue.poll()); // consuming message
            lock.unlock();
            empty.release();
        }
    }

    private Item produceNewItem() {
        return new Item("new Message!");
    }

    private static class Item {
        private String name;

        public Item(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}




