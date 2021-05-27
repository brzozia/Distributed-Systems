package messages;

import akka.actor.typed.ActorRef;

public class GetSatelliteData implements Message{
    public int satelliteId;
    public ActorRef<Message> replyTo;

    public GetSatelliteData(int satelliteId,ActorRef<Message> replyTo) {
        this.satelliteId = satelliteId;
        this.replyTo = replyTo;
    }
}
