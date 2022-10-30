import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class RabbitMQChannelFactory extends BasePooledObjectFactory<Channel> {
    private ConnectionFactory connectionFactory = new ConnectionFactory();

    @Override
    public Channel create() throws Exception {
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("ming");
        connectionFactory.setPassword("123456");
        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<Channel>(channel);
    }
}
