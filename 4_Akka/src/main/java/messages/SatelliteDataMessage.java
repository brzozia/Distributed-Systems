package messages;

public class SatelliteData implements Message {
    public final SatelliteAPI.Status status;

    public SatelliteData(SatelliteAPI.Status status) {
        this.status = status;
    }
}
