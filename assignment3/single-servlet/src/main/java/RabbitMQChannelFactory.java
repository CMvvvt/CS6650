import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class RabbitMQChannelFactory extends  BasePooledObjectFactory<Channel>{
    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private final static String HOST = "3.95.245.0";
    private final static String PASSWORD = "983150";
    private final static String USERNAME = "ming";
    private final static Integer THREADS = 500;
    private final static String GUEST = "guest";
    private final static String LOCALHOST = "localhost";
    @Override
    public Channel create() throws Exception {
        connectionFactory.setHost(HOST);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }
}
