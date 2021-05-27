package messages;

import akka.actor.typed.ActorRef;

public final class Request implements Message{
    public final int queryId;
    public final int firstSatID;
    public final int range;
    public final int timeout;
    public final ActorRef<SatelliteReply> replyTo;

    public Request(int queryId, int firstSatID, int range, int timeout, ActorRef<SatelliteReply> replyTo) {
        this.queryId = queryId;
        this.firstSatID = firstSatID;
        this.range = range;
        this.timeout = timeout;
        this.replyTo = replyTo;
    }
}