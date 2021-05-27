import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.Message;
import messages.SatelliteReplyMessage;

public class MonitoringStation extends AbstractBehavior<SatelliteReplyMessage> {
    private String name;
    private int queryId;
    private ActorRef<Message> dispatcher;

    private MonitoringStation(ActorContext<SatelliteReplyMessage> context, String name, ActorRef<Message> dispatcher) {
        super(context);
        this.name = name;
        this.dispatcher = dispatcher;
        this.queryId = 0;
    }

    public static Behavior<SatelliteReplyMessage> create(String name, ActorRef<Message> dispatcher) {
        return Behaviors.setup(context-> new MonitoringStation(context, name, dispatcher));
    }

    @Override
    public Receive<SatelliteReplyMessage> createReceive() {
        return newReceiveBuilder().onMessage(SatelliteReplyMessage.class, this::onSatelliteReply).build();
    }

    private Behavior<SatelliteReplyMessage> onSatelliteReply(SatelliteReplyMessage reply) {
        System.out.println("station name: "+this.name);
        System.out.println("jakiś czas");
        System.out.println("Errors: "+reply.satelliteStatus.keySet().size());
        reply.satelliteStatus.forEach((key,val) -> System.out.println(key + " : "+val));
        return this;
    }


}
