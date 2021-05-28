package messages;

public class DatabaseMessage implements Message{
    public int satelliteId;
    public int errorsNumber;

    public DatabaseMessage(int satelliteId, int errorsNumber) {
        this.satelliteId = satelliteId;
        this.errorsNumber = errorsNumber;
    }
}
