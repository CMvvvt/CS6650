package io.swagger.client.assignment1_client.part2;

public class PerformanceData {
    private double mean;
    private long median;
    private double throughput;
    private long p99;
    private long min;
    private long max;

    public PerformanceData(double mean, long median, double throughput, long p99, long min, long max) {
        this.mean = mean;
        this.median = median;
        this.throughput = throughput;
        this.p99 = p99;
        this.min = min;
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public long getMedian() {
        return median;
    }

    public void setMedian(long median) {
        this.median = median;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public long getP99() {
        return p99;
    }

    public void setP99(long p99) {
        this.p99 = p99;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}
