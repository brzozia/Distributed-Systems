package pl.travel;

import pl.travel.APIs.AeroDataAPI;
import pl.travel.APIs.AmadeusAPI;
import pl.travel.APIs.GeoDBAPI;

import java.util.List;

public class SourcesAPI {
    private final AmadeusAPI amadeus;
    private final GeoDBAPI geo;
    private final AeroDataAPI aero;

    public SourcesAPI() {
        this.amadeus = new AmadeusAPI();
        this.geo = new GeoDBAPI();
        this.aero = new AeroDataAPI();
    }

    public List<Integer> getCityAirports(String input){
        return aero.getAirports(input);
    };

    public String getAllFlightsResults(String origin, String departure, boolean oneWay, int duration){
        return amadeus.getFlights(origin, departure, oneWay, duration);
//        .foreach(
        int destID = geo.getDestID(lon, lat);
        geo.getDistance(originID, destID);
//        );
    };

}
