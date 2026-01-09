package org.example.practiceQuestions;

public class CustomSemaphore {
    public static void main(String[] args) throws InterruptedException {
        Semaphore customSemaphore = new Semaphore(5, 3);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    customSemaphore.acquire();
                    System.out.println("hello from thread1");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        Thread.sleep(5000);
        new Thread(() -> {
            try {
                System.out.println("releasing few locks");
                customSemaphore.release();
                customSemaphore.release();
                customSemaphore.release();
                customSemaphore.release();
                customSemaphore.release();
                customSemaphore.release();
            } catch (RuntimeException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    private static class Semaphore {
        private final int max;
        private int count;

        public Semaphore(int max, int initial) {
            this.max = max;
            this.count = initial;
        }

        public synchronized void acquire() throws InterruptedException {
            while (count == 0) {
                wait();
            }
            count--;
            notify();
        }

        public synchronized void release() throws InterruptedException {
            while (this.count == this.max) {
                wait();
            }
            count++;
            notify();
        }
    }
}
