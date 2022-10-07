package io.swagger.client.assignment1_client;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.assignment1_client.part2.Performance;
import io.swagger.client.model.LiftRide;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PostThread implements Runnable {

    private SkiersApi skiersApi;
    private Integer numberOfPosts;
    private CountDownLatch latch;
    private LinkedBlockingQueue<RandomLiftRide> queue;
    private AtomicInteger counter;
    private LinkedBlockingQueue<Performance> performanceQueue;
    private int failedTimes;

    public PostThread(Integer numberOfPosts, String basePath, CountDownLatch latch, LinkedBlockingQueue queue, AtomicInteger counter, LinkedBlockingQueue performanceQueue) {
        this.skiersApi = new SkiersApi();
        this.skiersApi.getApiClient().setBasePath(basePath);
        this.numberOfPosts = numberOfPosts;
        this.latch = latch;
        this.queue = queue;
        this.counter = counter;
        this.failedTimes = 0;
        this.performanceQueue = performanceQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.numberOfPosts; i++) {
            // end the thread if there is no data in the queue
            if (queue.isEmpty()) {
                break;
            }
            RandomLiftRide randomLiftRide = null;
            try {
                randomLiftRide = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LiftRide liftRide = randomLiftRide.getBody();
            Integer resortID = randomLiftRide.getResortID();
            String seasonID = randomLiftRide.getSeasonID();
            String dayID = randomLiftRide.getDayID();
            Integer skierID = randomLiftRide.getSkierID();
            ApiResponse res = null;
            try {
                long start = System.currentTimeMillis();
                res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, resortID, seasonID, dayID, skierID);
                long end = System.currentTimeMillis();
                int code = res.getStatusCode();
                if (code == 201) {
                    if (this.performanceQueue != null) {
                        this.performanceQueue.add(new Performance(start, end, "POST", code));
                    }
                    this.counter.incrementAndGet();
                    System.out.println(code + " " + (end - start) + "ms");
                }
            } catch (ApiException e) {
                this.failedTimes += 1;
                System.out.println(e);
                if (this.failedTimes == 5) {
                    break;
                }
                continue;
            }
        }
        this.latch.countDown();
    }
}
