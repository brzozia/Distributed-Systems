package messages;

import API.SatelliteAPI;

import java.util.Map;

public final class SatelliteReplyMessage implements Message {
    public final int queryId;
    public final Map<Integer, SatelliteAPI.Status> satelliteStatus;
    public final double inTimeResponds;

    public SatelliteReplyMessage(int queryId, Map<Integer, SatelliteAPI.Status> satelliteStatus, double inTimeResponds) {
        this.queryId = queryId;
        this.satelliteStatus = satelliteStatus;
        this.inTimeResponds = inTimeResponds;
    }
}
