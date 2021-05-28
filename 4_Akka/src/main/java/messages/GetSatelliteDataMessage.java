package messages;

import akka.actor.typed.ActorRef;

public class GetSatelliteDataMessage implements Message{
    public int satelliteId;
    public double time;
    public ActorRef<SatelliteDataMessage> replyTo;

    public GetSatelliteDataMessage(int satelliteId, ActorRef<SatelliteDataMessage> replyTo, double time) {
        this.satelliteId = satelliteId;
        this.replyTo = replyTo;
        this.time = time;
    }
}
