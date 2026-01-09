package org.example.semaphore.producerConsumer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerWithLock {

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            threads.add(
                    new Thread(() -> {
                        try {
                            Thread.sleep(100);
                            pc.produce("test1");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
        }

        for (int i = 0; i < 6; i++) {
            threads.add(
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            pc.consume();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
        }


        for (Thread thread : threads) {
            thread.start();
        }

    }

    private static class ProducerConsumer {
        Queue<String> queue = new ArrayDeque<>();
        int capacity = 5;

        public synchronized void produce(String msg) throws InterruptedException {
            while (true) {
                while (queue.size() == capacity) {
                    wait();
                }

                System.out.println("---- offering message ----");
                queue.offer(msg);
                notifyAll();
            }

        }

        public synchronized void consume() throws InterruptedException {
            while (true) {
                while (queue.isEmpty()) {
                    wait();
                }
                System.out.println("---- pooling message ----");
                System.out.println(queue.poll());
                notifyAll();
            }
        }
    }
}
