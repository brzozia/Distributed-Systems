package messages;

import akka.actor.typed.ActorRef;

public class DelegationReplyMessage implements Message{
    public final RequestMessage request;
    public int id;
    public final ActorRef<Message> replyTo;

    public DelegationReplyMessage(RequestMessage satelliteReply, int id, ActorRef<Message> replyTo) {
        this.request = satelliteReply;
        this.id = id;
        this.replyTo = replyTo;
    }
    
}
