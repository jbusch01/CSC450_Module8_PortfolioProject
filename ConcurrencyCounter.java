/** 
 * Concurrency counter written in Java
 * Demonstrates basic Java concurrency, synchronized thread coordination,
 * and data hanbdling.
 */

public class ConcurrencyCounter {

    private static final Object lock = new Object();
    private static boolean countUpFinished = false;

    public static void main(String[] args) {
        // Thread 1 counts up from 1 to 20
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("--- Thread 1: Starting Count ---");
                    for (int i = 1; i <= 20; i++) {
                        System.out.println(i);
                        try {
                            Thread.sleep(100); // Execution delay for readability
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    countUpFinished = true;
                    lock.notify(); // Singal for Thread 2 to start
                }
            }
        });

        // Thread 2: Counts down from 20 to 0 after Thrrad 1 finishes
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    while (!countUpFinished) {
                        try {
                            lock.wait(); // Wait for signal from Thread 1
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("\n--- Thread 2: Starting Count ---")
                    for (int i = 20; i >= 0; i--) {
                        System.out.println(i);
                        try {
                            Thread.sleep(100); // Execution delay
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        });

        // start both threads
        thread2.start(); // Thread 2 starts and waits for thread 1
        thread1.start(); // Thread 1 starts, counts, finishes, then notifies thread 2
    }
}