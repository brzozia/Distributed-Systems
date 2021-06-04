package pl.travel.data.amadeusAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airports {
    private List<Airport> data;

    public List<Airport> getData() {
        return data;
    }

    public void setData(List<Airport> data) {
        this.data = data;
    }

    public Airports() {
    }
}
