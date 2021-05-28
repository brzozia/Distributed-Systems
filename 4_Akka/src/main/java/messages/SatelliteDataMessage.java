package messages;

import API.SatelliteAPI;

public class SatelliteDataMessage implements Message {
    public final SatelliteAPI.Status status;
    public final int id;

    public SatelliteDataMessage(SatelliteAPI.Status status, int id) {
        this.status = status;
        this.id = id;
    }
}
