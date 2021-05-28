package messages;

public class SendMessage implements Message{
    public final int first;
    public final int range;
    public final long timeout;

    public SendMessage(int first, int range, long timeout) {
        this.first = first;
        this.range = range;
        this.timeout = timeout;
    }
}
