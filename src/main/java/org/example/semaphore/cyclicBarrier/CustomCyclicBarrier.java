package org.example.semaphore.cyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CustomCyclicBarrier {
    public static void main(String[] args) {
        int numberOfThreads = 3;

        List<Thread> threads = new ArrayList<>();
        Barrier barrier = new Barrier(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    private static class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0);
        private int counter = 0;

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void waitForOthers() throws InterruptedException {
            boolean isLastWorker = false;

            synchronized (this) {
                counter++;
                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            }

            if (isLastWorker) {
                semaphore.release(numberOfWorkers - 1);
            } else {
                semaphore.acquire();
            }
        }
    }

    private static class CoordinatedWorkRunner implements Runnable {
        Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                task();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void task() throws InterruptedException {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName()
                    + " part 1 of the work is finished");

            barrier.waitForOthers();

            // Performing Part2
            System.out.println(Thread.currentThread().getName()
                    + " part 2 of the work is finished");
        }
    }
}

