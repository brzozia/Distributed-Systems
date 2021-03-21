import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;


public class Supplier {
    public static void main(String[] argv) throws IOException, TimeoutException {
        String name = argv[0];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter list of products for sale (separated by a space): ");
        ArrayList<String> productList = new ArrayList<>(Arrays.asList(br.readLine().split(" ")));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1); // to manage orders with load-balancing order

        String SUPPLIERS_EXCHANGE_NAME = "fromSuppliers"; // exchange from Teams
        channel.exchangeDeclare(SUPPLIERS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // set supplier's name in properties
        Map<String,Object> messageProps = new HashMap<>();
        messageProps.put("name", name);
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().headers(messageProps).build();

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                Object from = properties.getHeaders().get("name");
                System.out.println("Received: " + message + " from: " + from);
                channel.basicAck(envelope.getDeliveryTag(), false);

                if(!from.toString().equals("admin")) {
                    channel.basicPublish(SUPPLIERS_EXCHANGE_NAME, "r." + from, props, message.getBytes("UTF-8"));
                    System.out.println("Send message to: " + "r." + from);
                }
            }
        };

        // exchange and queues to receive orders from teams
        String TEAMS_EXCHANGE_NAME = "fromTeams";
        channel.exchangeDeclare(TEAMS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        productList.forEach((product) -> { // make queue for each product order from teams
            try {
                channel.queueDeclare(product, true, false, false, null);
                channel.queueBind(product, TEAMS_EXCHANGE_NAME, "o."+product);
                System.out.println("created queue: " + product + " with key: " + "o."+product);
                channel.basicConsume(product, false, consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // queue for receiving messages from administrator
        String ADMIN_SUPPLIERS_EXCHANGE_NAME = "fromAdminToSuppliers";
        channel.exchangeDeclare(ADMIN_SUPPLIERS_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ADMIN_SUPPLIERS_EXCHANGE_NAME, "");
        System.out.println("created fanout queue: " + queueName);
        channel.basicConsume(queueName, false, consumer);


        System.out.println("Waiting for messages...");
    }
}
