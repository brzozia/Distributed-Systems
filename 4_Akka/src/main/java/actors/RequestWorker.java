package actors;

import API.SatelliteAPI;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.*;

import java.util.HashMap;
import java.util.Map;

public class RequestWorker extends AbstractBehavior<Message> {
    private int queryId;
    private int count;
    private int range;
    private int inTime;
    private Map<Integer, SatelliteAPI.Status> map;
    private ActorRef<Message> replyToStation;

    public RequestWorker(ActorContext<Message> context) {
        super(context);
    }

    public static Behavior<Message> create() {
        return Behaviors.setup(RequestWorker::new);
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(RequestMessage.class, this::onRequest)
                .onMessage(SatelliteDataMessage.class, this::onReply).build();
    }

    private Behavior<Message> onRequest(RequestMessage request) {
        int first = request.firstSatID;
        this.queryId = request.queryId;
        this.replyToStation = request.replyTo;
        this.range = request.range;
        this.count = 0;
        this.inTime = 0;

        this.map = new HashMap<>();

        for (int i = first; i < first + request.range; i++) {
            ActorRef<Message> worker = getContext().spawn(
                    Behaviors.supervise(SatelliteWorker.create())
                            .onFailure(Exception.class, SupervisorStrategy.restart()), "satelliteworker"+request.queryId + "" + i+""+ProcessHandle.current().pid());

            worker.tell(new DelegationRequestMessage(request,i,getContext().getSelf()));

        }
        return this;
    }

    private Behavior<Message> onReply(SatelliteDataMessage reply) {
        if (reply.status != SatelliteAPI.Status.OK) {
            map.put(reply.id, reply.status);
        }
        if(reply.id != -1){
            inTime += 1;
        }
        count += 1;
        if (count == this.range) {
            this.replyToStation.tell(new SatelliteReplyMessage(this.queryId, this.map, (inTime*100/count) ));
        }
        return this;
    }
}
