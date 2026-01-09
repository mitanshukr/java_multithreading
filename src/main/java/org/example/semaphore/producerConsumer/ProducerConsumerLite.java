package org.example.semaphore.producerConsumer;

import java.util.concurrent.Semaphore;

public class ProducerConsumerLite {
    public static void main(String[] args) {

        ProducerConsumerSingle prodCons = new ProducerConsumerSingle();

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

class ProducerConsumerSingle {
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(1);
    Item item = null;

    public void producer() throws InterruptedException {
        while (true) {
            empty.acquire();
            System.out.println("--- producing message ----");
//            Thread.sleep(1000);
            item = produceNewItem();
            full.release();
        }
    }

    public void consumer() throws InterruptedException {
        while (true) {
            full.acquire();
            System.out.println("#### consuming message #####");
            Thread.sleep(2000);
            System.out.println(item); // consuming message
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




