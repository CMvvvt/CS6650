import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import models.Skier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Consumer {
    private final static String QUEUE_NAME = "SkierQueue";
    private final static Integer THREADS = 500;

    public static void main(String[] args) throws Exception{
        ConcurrentHashMap<String, CopyOnWriteArrayList<Skier>> map = new ConcurrentHashMap<>();
        ConnectionFactory factory = new ConnectionFactory();
        Gson gson = new Gson();
        factory.setHost("54.190.211.140");
        factory.setUsername("ming");
        factory.setPassword("983150");

        Connection connection = factory.newConnection();
        Runnable runnable = () -> {
            Channel channel;
            try{
                channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    Skier skier = gson.fromJson(message, Skier.class);
                    System.out.println(" [x] Received '" + skier.toString() + "'");
                    map.putIfAbsent(skier.getSkierID(), new CopyOnWriteArrayList<>());
                    map.get(skier.getSkierID()).add(skier);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                };
                channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}