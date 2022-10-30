package io.swagger.client.assignment1_client.part2;

public class Performance {
    private long startTime;
    private long endTime;
    private String type;
    private int code;
    private long latency;

    public Performance(long startTime, long endTime, String type, int code) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.code = code;
        this.latency = endTime - startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }
}

