package io.swagger.client.assignment1_client.part1;

import io.swagger.client.assignment1_client.PostThread;
import io.swagger.client.assignment1_client.RandomLiftRide;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedClient {
    final private static int NUM_THREADS = 32;
    final private static int POSTS_PER_THREADS = 1000;
    final private static int NUM_THREADS_SECOND = 256;
    final private static int POSTS_PER_THREADS_SECOND = 10000;
    final private static int NUM_POSTS = 200_000;
    final private static String LOCAL_PATH = "http://localhost:8080/single_servlet_war_exploded/";
    final private static String EC2_PATH = "http://35.175.243.180/single-servlet_war/";

    private String path;

    public MultithreadedClient(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    private void postWith200Threads() throws InterruptedException {
        LinkedBlockingQueue<RandomLiftRide> queue = new LinkedBlockingQueue<>();
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < NUM_POSTS; i++) {
            queue.add(new RandomLiftRide());
        }
        long start = System.currentTimeMillis();
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(NUM_THREADS_SECOND);

        runThread(queue, POSTS_PER_THREADS, counter, NUM_THREADS, latch1);
        latch1.await();
        runThread(queue, POSTS_PER_THREADS_SECOND, counter, NUM_THREADS_SECOND, latch2);
        latch2.await();
        long end = System.currentTimeMillis();
        long time = end - start;
        int size = queue.size();
        System.out.println("*** Output for part 1 ***");
        System.out.println();
        System.out.println("Threads used in this program: "+  NUM_THREADS  + " + " +  NUM_THREADS_SECOND);
        System.out.println("Time elapsed for " + (NUM_POSTS - size) + " post requests is: " + time + "ms");
        System.out.println("Successful requests sent: " + counter.toString());
        System.out.println("Failed requests sent: " + (NUM_POSTS - counter.get()));
        System.out.println("Total throughput is: " + NUM_POSTS * 1000 / time + " per second");
    }

    private void runThread(LinkedBlockingQueue<RandomLiftRide> queue, int numberOfPosts, AtomicInteger counter, int numThreadsSecond, CountDownLatch latch) throws InterruptedException {
        for (int i = 0; i < numThreadsSecond; i++) {
            PostThread postThread = new PostThread(numberOfPosts, this.path , latch, queue, counter, null);
            Thread thread = new Thread(postThread);
            thread.start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MultithreadedClient post = new MultithreadedClient(EC2_PATH);
        post.postWith200Threads();
    }
}
