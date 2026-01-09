package org.example.practiceQuestions;

import java.util.PriorityQueue;

public class ThreadSafeCallback {
    public static void main(String[] args) throws InterruptedException {
        CallbackExecutor cbe = new CallbackExecutor();

        new Thread(() -> {
            try {
                cbe.start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            Callback cb = new Callback(8, "first message!");
            cbe.registerCallback(cb);
        }).start();

        Thread.sleep(3000);

        new Thread(() -> {
            Callback cb = new Callback(2, "early exec message!");
            cbe.registerCallback(cb);
        }).start();

        new Thread(() -> {
            Callback cb = new Callback(60, "last exec message! with 60sec delay");
            cbe.registerCallback(cb);
        }).start();
    }

    private static class CallbackExecutor {
        PriorityQueue<Callback> queue = new PriorityQueue<>((o1, o2) -> Math.toIntExact(o1.executeAt - o2.executeAt));

        public void start() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (queue.isEmpty()) {
                        wait();
                    }

                    while (!queue.isEmpty()) {
                        long sleepFor = queue.peek().executeAt - System.currentTimeMillis();

                        if (sleepFor <= 0) {
                            break;
                        }
                        wait(sleepFor);
                        System.out.println("trigger was called....");
                    }

                    Callback callback = queue.poll();

                    System.out.println("Executed at " + System.currentTimeMillis() / 1000 + " required At " + callback.executeAt / 1000
                            + " :message: " + callback.message);
                }
            }
        }

        public synchronized void registerCallback(Callback callback) {
            queue.offer(callback);
            notify();
        }
    }

    static class Callback {
        long executeAt;
        String message;

        public Callback(long executeAfterInSec, String message) {
            this.executeAt = System.currentTimeMillis() + executeAfterInSec * 1000;
            this.message = message;
        }
    }
}
