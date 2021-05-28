package messages;

import akka.actor.typed.ActorRef;

public final class RequestMessage implements Message {
    public final int queryId;
    public final int firstSatID;
    public final int range;
    public final long timeout;
    public final ActorRef<Message> replyTo;

    public RequestMessage(int queryId, int firstSatID, int range, long timeout, ActorRef<Message> replyTo) {
        this.queryId = queryId;
        this.firstSatID = firstSatID;
        this.range = range;
        this.timeout = timeout;
        this.replyTo = replyTo;
    }
}