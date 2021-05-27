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
    private final ActorRef<Message> dispatcher;
    private int queryId;
    private int count;
    private int range;
    private Map<Integer,SatelliteAPI.Status> map;
    private ActorRef<SatelliteReplyMessage> replyToStation;

    public RequestWorker(ActorContext<Message> context, ActorRef<Message> dispatcher) {
        super(context);
        this.dispatcher = dispatcher;
    }

    public static Behavior<Message> create(ActorRef<Message> dispatcher) {
        return Behaviors.setup(context -> new RequestWorker(context, dispatcher));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(RequestDelegation.class, this::onRequest)
                .onMessage(SatelliteDataMessage.class, this::onReply).build();
    }

    private Behavior<Message> onRequest(RequestDelegation request){
        //make actors take data
        int first = request.request.firstSatID;
        this.queryId = request.request.queryId;
        this.replyToStation = request.request.replyTo;
        this.range = request.request.range;
        this.count = 0;
        this.map = new HashMap<>();

        for(int i=first; i<first+request.request.range; i++){
            ActorRef<GetSatelliteDataMessage> worker = getContext().spawn(
                    Behaviors.supervise(SatelliteWorker.create())
                            .onFailure(Exception.class, SupervisorStrategy.restart()), String.valueOf(i));
            worker.tell(new GetSatelliteDataMessage(i, getContext().getSelf()));
        }
        return this;
    }

    private Behavior<Message> onReply(SatelliteDataMessage reply){
        //manage data
        if(reply.status== SatelliteAPI.Status.OK){
            map.put(reply.id,reply.status);
        }
        count+=1;
        if(count==this.range) {
            dispatcher.tell(new DelegationReplyMessage(new SatelliteReplyMessage(this.queryId, this.map, time), this.replyToStation));
        }
        return this;
    }
}
