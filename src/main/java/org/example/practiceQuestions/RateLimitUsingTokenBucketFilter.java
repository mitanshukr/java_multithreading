package org.example.practiceQuestions;

// https://github.com/mitanshukr/edu/blob/c72febca02fd41ca99d0c98155bb2010b87bb7a8/Java%20Multithreading%20for%20Senior%20Engineering%20Interviews%20-%20Learn%20Interactively/31_Rate_Limiting_Using_Token_Bucket_Filter.pdf


import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class RateLimitUsingTokenBucketFilter {
    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(5);

        Thread t = new Thread(() -> {
            try {
                rateLimiter.producer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t.setDaemon(true);
        t.start();


        new Thread(() -> {
            while (true) {
                try {
                    rateLimiter.getToken();
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

//        new Thread(() -> {
//            while (true) {
//                try {
//                    rateLimiter.getToken();
//                    Thread.sleep(2500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();

    }

    private static class RateLimiter {
        private final int capacity;
        private int tokens = 0;

        public RateLimiter(int capacity) {
            this.capacity = capacity;
        }

        private void producer() throws InterruptedException {
            while (true) {
                try {
                    synchronized (this) {
                        if (tokens < capacity) {
                            tokens++;
                            notifyAll();
                        }
                    }

                    System.out.println("here----");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private synchronized void getToken() throws InterruptedException {
            while (tokens == 0) {
                wait();
            }

            tokens--;
            System.out.println("current count: " + tokens);
//            notifyAll();
        }
    }
}
