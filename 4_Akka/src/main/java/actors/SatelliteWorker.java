package actors;

import API.SatelliteAPI;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.DelegationRequestMessage;
import messages.GetSatelliteDataMessage;
import messages.Message;
import messages.SatelliteDataMessage;

import java.time.Duration;

public class SatelliteWorker extends AbstractBehavior<Message> {
    private ActorRef<Message> requestWorker;

    public SatelliteWorker(ActorContext<Message> context) {
        super(context);
    }
    public static Behavior<Message> create() {
        return Behaviors.setup(SatelliteWorker::new);
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(DelegationRequestMessage.class, this::onRequest)
                .onMessage(GetSatelliteDataMessage.class, this::onGetSatelliteRequest)
                .onMessage(SatelliteDataMessage.class, this::onSatelliteRequest)
                .build();
    }

    private Behavior<Message> onRequest(DelegationRequestMessage request){
        requestWorker = request.replyTo;
        getContext().ask(
                SatelliteDataMessage.class,
                getContext().getSelf(),
                Duration.ofMillis(request.request.timeout),
                (ActorRef<SatelliteDataMessage> ref) -> new GetSatelliteDataMessage(request.id, ref, 0),
                (response, throwable) -> {
                    if (response != null) {
                        return new SatelliteDataMessage(response.status, response.id);
                    } else {
                        return new SatelliteDataMessage(SatelliteAPI.Status.OK, -1);
                    }
                }
        );
        return this;
    }

    private Behavior<Message> onGetSatelliteRequest(GetSatelliteDataMessage request){
        SatelliteAPI.Status status = SatelliteAPI.getStatus(request.satelliteId);
        request.replyTo.tell(new SatelliteDataMessage(status, request.satelliteId));
        return this;
    }

    private Behavior<Message> onSatelliteRequest(SatelliteDataMessage request){
        requestWorker.tell(request);
        return this;
    }
}
