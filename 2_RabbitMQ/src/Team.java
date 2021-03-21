import com.rabbitmq.client.*;
import  com.rabbitmq.client.AMQP.BasicProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Team {
    public static void main(String[] argv) throws IOException, TimeoutException {
        String name = argv[0];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // consumer to consume replies from suppliers
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message from: " + properties.getHeaders().get("name") + " processed order: " + message );
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        // create exchange for sending orders to suppliers
        String TEAMS_EXCHANGE_NAME = "fromTeams";
        channel.exchangeDeclare(TEAMS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String order = "";

        // create queue and exchange for replies from suppliers
        String SUPPLIERS_EXCHANGE_NAME = "fromSuppliers"; // exchange from Suppliers
        channel.exchangeDeclare(SUPPLIERS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(name, true, false, false, null);
        channel.queueBind(name, SUPPLIERS_EXCHANGE_NAME, "r."+name);
        System.out.println("created queue: " + name + " with key: " + "r."+name);
        channel.basicConsume(name, false, consumer);

        // queue for receiving messages from administrator
        String ADMIN_TEAMS_EXCHANGE_NAME = "fromAdminToTeams";
        channel.exchangeDeclare(ADMIN_TEAMS_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ADMIN_TEAMS_EXCHANGE_NAME, "");
        System.out.println("created fanout queue: " + queueName);
        channel.basicConsume(queueName, false, consumer);

        // set team's name in properties
        Map<String,Object> messageProps = new HashMap<>();
        messageProps.put("name", name);
        BasicProperties props = new BasicProperties.Builder().headers(messageProps).build();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // enter messages to send to suppliers
        while (!order.equals("close")) {
            System.out.println("Enter order: ");
            order = br.readLine();

            channel.basicPublish(TEAMS_EXCHANGE_NAME, "o."+order, props, order.getBytes("UTF-8"));
            System.out.println("Sent: " + order);
        }

    }
}
