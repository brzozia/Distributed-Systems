package mainApp;

import actors.Database;
import actors.Dispatcher;
import actors.MonitoringStation;
import akka.actor.typed.*;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import messages.DatabaseMessage;
import messages.GetDatabaseInfoMessage;
import messages.Message;
import messages.SendMessage;

import java.io.File;
import java.sql.*;
import java.util.Random;

public class MainApp {

    public static Behavior<Void> create() throws SQLException, ClassNotFoundException {
//        File configFile = new File("src/main/resources/application.conf");
//        Config config = ConfigFactory.parseFile(configFile);

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        System.out.println("Opened database successfully");
        Statement statement = connection.createStatement();
        String createTable = "CREATE TABLE IF NOT EXISTS errorsTable (satellite_id INTEGER PRIMARY KEY, errors INTEGER);";
        statement.execute(createTable);

        ResultSet set = statement.executeQuery("SELECT * FROM  errorsTable");

        if(!set.next()){
            for (int i = 100; i < 200; i++) {
                statement.execute("INSERT INTO errorsTable (satellite_id, errors) VALUES ("+i+","+0+" );");
            }
        }
        else{
            for (int i = 100; i < 200; i++) {
                statement.execute("UPDATE errorsTable set errors = 0 WHERE satellite_id ="+i);
            }
        }

        statement.close();

        return Behaviors.setup(
                context -> {
                    // create dispatcher
                    ActorRef<Message> dispatcher = context.spawn(Dispatcher.create(),"dispatcher", DispatcherSelector.fromConfig("my-dispatcher"));
//                            context.spawn(Dispatcher.create(), "dispatcher");

                    ActorRef<Message> s1 = context.spawn(MonitoringStation.create("station1", dispatcher, connection), "station1");
                    ActorRef<Message> s2 = context.spawn(MonitoringStation.create("station2", dispatcher, connection), "station2");
                    ActorRef<Message> s3 = context.spawn(MonitoringStation.create("station3", dispatcher, connection), "station3");


//                    s1.tell(new SendMessage(100, 20, 300));
//                    s2.tell(new SendMessage(100, 40, 300));

                    Random rand = new Random();
                    s1.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));
                    s1.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));
                    s2.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));
                    s2.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));
                    s3.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));
                    s3.tell(new SendMessage(100+ rand.nextInt(50), 50, 300));

                    Thread.sleep(3000);
                    for(int i=100;i<200;i++)
                        s1.tell(new GetDatabaseInfoMessage(i,s1));

                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                });
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ActorSystem.create(MainApp.create(), "mainApp");
    }
}

