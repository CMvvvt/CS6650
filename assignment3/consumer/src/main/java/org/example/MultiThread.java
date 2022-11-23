package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.example.models.Skier;
import org.example.models.SkierDao;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThread implements Runnable{
    private String queueName;
    private Gson gson = new Gson();
    private final Connection connection;

    public MultiThread(String queueName, Connection connection) {
        this.queueName = queueName;
        this.connection = connection;
    }

    @Override
    public void run() {
        try{
            final Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Skier skier = gson.fromJson(message, Skier.class);
                SkierDao skierDao = new SkierDao(skier, Consumer.getPool());
                skierDao.createSkier();
                System.out.println("Thread " + Thread.currentThread().getId() + " received");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });

        } catch (IOException e) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
