package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.*;

import java.sql.Connection;
import java.sql.Statement;

public class MonitoringStation extends AbstractBehavior<Message> {
    private final String name;
    private int queryId;
    private final ActorRef<Message> dispatcher;
    private final ActorRef<Message> database;
    private long start;
    private long finish;

    private MonitoringStation(ActorContext<Message> context, String name, ActorRef<Message> dispatcher, Connection connection) {
        super(context);
        this.name = name;
        this.dispatcher = dispatcher;
        this.database = getContext().spawn(
                Behaviors.supervise(Database.create(connection))
                        .onFailure(Exception.class, SupervisorStrategy.restart()), "db_" + name);
        this.queryId = 0;
    }

    public static Behavior<Message> create(String name, ActorRef<Message> dispatcher, Connection connection) {
        return Behaviors.setup(context -> new MonitoringStation(context, name, dispatcher, connection));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(SatelliteReplyMessage.class, this::onSatelliteReply)
                .onMessage(GetDatabaseInfoMessage.class, this::onDatabaseInfo)
                .onMessage(DatabaseMessage.class, this::onDatabaseReply)
                .onMessage(SendMessage.class, this::sendRequest).build();
    }

    private Behavior<Message> onSatelliteReply(SatelliteReplyMessage reply) {
        finish = System.currentTimeMillis();
        System.out.println("\nstation name: " + this.name);
        System.out.println(this.name+" Time: "+(finish-start));
        System.out.println(this.name+" Errors: " + reply.satelliteStatus.keySet().size());
        reply.satelliteStatus.forEach((key, val) -> {
            database.tell(new DatabaseMessage(key, 1));
            System.out.println(key + " : " + val);});
        System.out.println(this.name+" Replies in time: "+reply.inTimeResponds+"%\n");
        return this;
    }

    public Behavior<Message> sendRequest(SendMessage message) {
        this.queryId += 1;
        dispatcher.tell(new RequestMessage(queryId, message.first, message.range, message.timeout, getContext().getSelf()));
        start = System.currentTimeMillis();
        return this;
    }

    private Behavior<Message> onDatabaseInfo(GetDatabaseInfoMessage request){
        database.tell(request);
        return this;
    }

    private Behavior<Message> onDatabaseReply(DatabaseMessage request){
        if(request.errorsNumber>0)
            System.out.println("station id: "+request.satelliteId +" errors:  "+request.errorsNumber);
        return this;
    }

}
