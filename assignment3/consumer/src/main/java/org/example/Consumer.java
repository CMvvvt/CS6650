package org.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import redis.clients.jedis.JedisPool;



public class Consumer {
    private final static String QUEUE_NAME = "SkierQueue";
    private final static String RABBITMQ_HOST = "3.95.245.0";
    private final static String LOCAL_HOST = "18.206.204.165";
    private final static String USERNAME = "ming";
    private final static String PASSWORD = "983150";
    private final static Integer THREADS = 500;
    private static JedisPool pool =  new JedisPool(LOCAL_HOST, 6379);
    private static ConnectionFactory factory = getConnectionFactory();

    public static void main(String[] args) throws Exception {
        Connection connection = factory.newConnection();
        System.out.println(" [*] Thread waiting for messages. To exit press CTRL+C!");
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(new MultiThread(QUEUE_NAME, connection));
            thread.start();
        }
    }

    private static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory;
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static ConnectionFactory getFactory() {
        return factory;
    }
}