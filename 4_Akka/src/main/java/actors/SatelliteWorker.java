import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.GetSatelliteDataMessage;
import messages.SatelliteDataMessage;

public class SatelliteWorker extends AbstractBehavior<GetSatelliteDataMessage> {


    public SatelliteWorker(ActorContext<GetSatelliteDataMessage> context) {
        super(context);
    }
    public static Behavior<GetSatelliteDataMessage> create() {
        return Behaviors.setup(SatelliteWorker::new);
    }

    @Override
    public Receive<GetSatelliteDataMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetSatelliteDataMessage.class, this::onRequest).build();
    }

    private Behavior<GetSatelliteDataMessage> onRequest(GetSatelliteDataMessage request){
        SatelliteAPI.Status status = SatelliteAPI.getStatus(request.satelliteId);
        //tu ten czas jeszcze
        request.replyTo.tell(new SatelliteDataMessage(status, request.satelliteId));
        return this;
    }
}
