package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.*;

import java.util.concurrent.Executor;

public class Dispatcher extends AbstractBehavior<Message> {
    private final Executor exec;

    public Dispatcher(ActorContext<Message> context) {
        super(context);
        exec = context.getSystem().dispatchers().lookup(DispatcherSelector.fromConfig("my-dispatcher"));
    }

    public static Behavior<Message> create() {
        return Behaviors.setup(Dispatcher::new);
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(RequestMessage.class, this::onRequest)
                .build();
    }

    private Behavior<Message> onRequest(RequestMessage request) {
//        exec.execute(() -> {
//            ActorRef<Message> worker = getContext().spawn(
//                Behaviors.supervise(RequestWorker.create())
//                        .onFailure(Exception.class, SupervisorStrategy.restart()), "worker_" + request.queryId+"_"+request.replyTo.path().name());
//            worker.tell(request);
//        });
        ActorRef<Message> worker = getContext().spawn(
                Behaviors.supervise(RequestWorker.create())
                        .onFailure(Exception.class, SupervisorStrategy.restart()), "worker_" + request.queryId+"_"+request.replyTo.path().name());
        worker.tell(request);
//
        return this;
    }
}
