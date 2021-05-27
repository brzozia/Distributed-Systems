package messages;

import java.util.Map;

public final class SatelliteReply implements Message {
    public final int queryId;
    public final Map<Integer, String> satelliteStatus;
    public final Double inTimeResponds;

    public SatelliteReply(int queryId, Map<Integer, String> satelliteStatus, Double inTimeResponds) {
        this.queryId = queryId;
        this.satelliteStatus = satelliteStatus;
        this.inTimeResponds = inTimeResponds;
    }
}
