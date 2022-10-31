package org.example;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.example.models.Skier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer {
    private final static String QUEUE_NAME = "SkierQueue";
    private final static String HOST = "52.34.123.9";
    private final static String USERNAME = "ming";
    private final static String PASSWORD = "983150";
    private final static Integer THREADS = 500;

    public static void main(String[] args) throws Exception{
        ConcurrentHashMap<String, CopyOnWriteArrayList<Skier>> map = new ConcurrentHashMap<>();
        ConnectionFactory factory = new ConnectionFactory();
        Gson gson = new Gson();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection();


        System.out.println(" [*] Thread waiting for messages. To exit press CTRL+C!");
        Runnable runnable = () -> {
            try{
                final Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    Skier skier = gson.fromJson(message, Skier.class);
                    System.out.println("Received " + skier.toString());
                    map.putIfAbsent(skier.getSkierID(), new CopyOnWriteArrayList<>());
                    map.get(skier.getSkierID()).add(skier);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                };
                channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

            } catch (IOException e) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, e);
            }
        };

        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}