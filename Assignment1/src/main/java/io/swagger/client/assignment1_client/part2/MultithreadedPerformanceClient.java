package io.swagger.client.assignment1_client.part2;

import com.opencsv.CSVWriter;
import io.swagger.client.assignment1_client.PostThread;
import io.swagger.client.assignment1_client.RandomLiftRide;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedPerformanceClient {
    final private static int NUM_THREADS = 32;
    final private static int POSTS_PER_THREADS = 1000;
    final private static int NUM_THREADS_SECOND = 50;
    final private static int POSTS_PER_THREADS_SECOND = 10000;
    final private static int NUM_POSTS = 200_000;
    final private static String LOCAL_PATH = "http://localhost:8080/lab1_java_servlet_war_exploded/";

    private void postWithMultipleThreads() throws InterruptedException, IOException {
        System.out.println("Program in process...");
        LinkedBlockingQueue<RandomLiftRide> queue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Performance> performanceQueue = new LinkedBlockingQueue<Performance>();
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < NUM_POSTS; i++) {
            queue.add(new RandomLiftRide());
        }
        long start = System.currentTimeMillis();
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(NUM_THREADS_SECOND);

        runThread(queue, POSTS_PER_THREADS, counter, NUM_THREADS, latch1, performanceQueue);
        latch1.await();
        runThread(queue, POSTS_PER_THREADS_SECOND, counter, NUM_THREADS_SECOND, latch2, performanceQueue);
        latch2.await();

        long end = System.currentTimeMillis();
        long time = end - start;

        PerformanceData data = getPerformanceAndWrite(performanceQueue, time);
        printData(data);
    }
    private void printData(PerformanceData data) {
        System.out.println("*** Output for part 2 ***");
        System.out.println("mean response time (milliseconds): " + data.getMean());
        System.out.println("median response time (milliseconds): " + data.getMedian());
        System.out.println("throughput (requests/second): " + data.getThroughput());
        System.out.println("p99 (99th percentile) response time: " + data.getP99());
        System.out.println("min response time (milliseconds): " + data.getMin());
        System.out.println("max response time (milliseconds): " + data.getMax());
    }
    private PerformanceData getPerformanceAndWrite(LinkedBlockingQueue<Performance> queue, long time) throws IOException {
        ArrayList<Performance> list = new ArrayList<>();
        long sum = 0;
        for(Performance performance: queue) {
            list.add(performance);
            sum += performance.getLatency();
        }
        Collections.sort(list, new Comparator<Performance>() {
            @Override
            public int compare(Performance o1, Performance o2) {
                return (int)(o1.getLatency() - o2.getLatency());
            }
        });
        int size = list.size();
        long median = list.get(size / 2).getLatency();
        double throughput = (double)size*1000 / time;
        double mean = (double)(sum) / size;
        long p99 = list.get((int) (0.01 * size)).getLatency();
        long min = list.get(0).getLatency();
        long max = list.get(size-1).getLatency();
        PerformanceData data = new PerformanceData(mean, median, throughput, p99, min, max);
        writeCVS(list);
        return data;
    }

    private void writeCVS(List<Performance> list) throws IOException {
        Collections.sort(list, new Comparator<Performance>() {
            @Override
            public int compare(Performance o1, Performance o2) {
                return (int) (o1.getStartTime() - o2.getStartTime());
            }
        });
        List<String[]> output = new ArrayList<>();
        String[] header = {"Start Time", "Request Type", "Latency", "Response Code"};
        output.add(header);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss.SSS");
        for(Performance performance: list){
            String start = sdf.format(new Date(performance.getStartTime()));
            String type = performance.getType();
            String latency = String.valueOf(performance.getLatency());
            String code = String.valueOf(performance.getCode());
            String[] curr = {start, type, latency, code};
            output.add(curr);
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("/Users/ming/IdeaProjects/CS6650/Assignment1/src/main/java/io/swagger/client/assignment1_client/part2/performance.csv"))) {
            writer.writeAll(output);
        }
    }

    private void runThread(LinkedBlockingQueue<RandomLiftRide> queue, int numberOfPosts, AtomicInteger counter,
                           int numThreadsSecond, CountDownLatch latch, LinkedBlockingQueue<Performance> performanceQueue) throws InterruptedException {
        for (int i = 0; i < numThreadsSecond; i++) {
            PostThread postThread = new PostThread(numberOfPosts, LOCAL_PATH, latch, queue, counter, performanceQueue);
            Thread thread = new Thread(postThread);
            thread.start();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        MultithreadedPerformanceClient post = new MultithreadedPerformanceClient();
        post.postWithMultipleThreads();
    }
}



