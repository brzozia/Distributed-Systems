package messages;

public class UpdateDatabaseMessage implements Message{
    public int stationId;
    public int newErrors;

    public UpdateDatabaseMessage(int stationId, int newErrors) {
        this.stationId = stationId;
        this.newErrors = newErrors;
    }
}
