package messages;

import akka.actor.typed.ActorRef;

public class GetDatabaseInfoMessage implements Message{
    public final int satelliteId;
    public final ActorRef<Message> replyTo;

    public GetDatabaseInfoMessage(int satelliteId, ActorRef<Message> replyTo) {
        this.satelliteId = satelliteId;
        this.replyTo = replyTo;
    }
}
