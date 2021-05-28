package pl.travel.data.amadeusAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.travel.data.amadeusAPI.FlightDestination;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDestinations {
    private List<FlightDestination> data;

    public List<FlightDestination> getData() {
        return data;
    }

    public void setData(List<FlightDestination> data) {
        this.data = data;
    }
}
