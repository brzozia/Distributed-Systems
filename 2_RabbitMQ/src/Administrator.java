import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Administrator {

    public static void main(String[] argv) throws IOException, TimeoutException {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String key = "*.*";

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message from: " + properties.getHeaders().get("name") + " processed order: " + message );
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        Map<String,Object> messageProps = new HashMap<>();
        messageProps.put("name", "admin");
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().headers(messageProps).build();

        // queue for receiving messages from suppliers
        String SUPPLIERS_EXCHANGE_NAME = "fromSuppliers";
        channel.exchangeDeclare(SUPPLIERS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(key, true, false, false, null);
        channel.queueBind(key, SUPPLIERS_EXCHANGE_NAME, key);

        // queue for receiving messages from teams
        String TEAMS_EXCHANGE_NAME = "fromTeams";
        channel.exchangeDeclare(TEAMS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueBind(key, TEAMS_EXCHANGE_NAME, key);
        System.out.println("created queue: " + key + " with key: " + key);

        channel.basicConsume(key, false, consumer);

        // exchange for sending messages to teams
        String ADMIN_TEAMS_EXCHANGE_NAME = "fromAdminToTeams";
        channel.exchangeDeclare(ADMIN_TEAMS_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // exchange for sending messages to teams
        String ADMIN_SUPPLIERS_EXCHANGE_NAME = "fromAdminToSuppliers";
        channel.exchangeDeclare(ADMIN_SUPPLIERS_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String message = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            System.out.println("Enter receiver (team/sup/both): ");
            String receiver = br.readLine();
            if(receiver.equals("close")){
                break;
            }
            System.out.println("Enter message: ");
            message = br.readLine();

            if(receiver.equals("team")) {
                channel.basicPublish(ADMIN_TEAMS_EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
                System.out.println("Sent: " + message);
            }
            else if(receiver.equals("sup")){
                channel.basicPublish(ADMIN_SUPPLIERS_EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
                System.out.println("Sent: " + message);
            }
            else if(receiver.equals("both")){
                channel.basicPublish(ADMIN_SUPPLIERS_EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
                channel.basicPublish(ADMIN_TEAMS_EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
                System.out.println("Sent: " + message);
            }
            else{
                System.out.println("Wrong receiver name");
            }
        }
        channel.close();
        connection.close();
    }
}
