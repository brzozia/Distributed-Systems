package pl.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import pl.travel.api.AirportInfoAPI;
import pl.travel.api.AmadeusAPI;
import pl.travel.api.BingImagesAPI;
import pl.travel.data.airportInfoAPI.AirportDetails;
import pl.travel.data.aeroDataAPI.Airports;
import pl.travel.data.amadeusAPI.FlightDestination;
import pl.travel.data.amadeusAPI.FlightDestinations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class SourcesAPI {
    private final AmadeusAPI amadeus;
//    private final AeroDataAPI aero;
    private final BingImagesAPI bing;
    private final AirportInfoAPI airport;

    public SourcesAPI() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> apiKeys = new ObjectMapper().readValue(new FileInputStream("src/main/resources/apiKeys/apiKeys.json"), Map.class);

        this.amadeus = new AmadeusAPI(restTemplate, apiKeys);
//        this.aero = new AeroDataAPI(restTemplate);
        this.bing = new BingImagesAPI(restTemplate, apiKeys);
        this.airport = new AirportInfoAPI(restTemplate, apiKeys);
    }

    public Airports getCityAirports(String input) throws Exception {
//        return aero.getAirports(input);
        return amadeus.getAirports(input);
    };

    public FlightDestinations getAllFlightsResults(String origin, String departure, boolean oneWay, String duration, Integer maxPrice) throws Exception {
        FlightDestinations flights= amadeus.getFlights(origin, departure, oneWay, duration,maxPrice);
        AirportDetails originDetails = airport.getDetails(origin);

        for(FlightDestination flight : flights.getData() ){
            AirportDetails airDetails = airport.getDetails(flight.getDestination());
            flight.setOrigin(bing.getPhoto(airDetails.getLocation()));
            flight.setDestination(airDetails.getName()+" |\n "+airDetails.getLocation());
            flight.setType(calculateDistance(airDetails.getLatitude(), airDetails.getLongitude(), originDetails.getLatitude(), originDetails.getLongitude()));
        }

        return flights;
    }


    public String calculateDistance(String slatA, String slonA, String slatB, String slonB){
        double latA = Double.parseDouble(slatA);
        double lonA = Double.parseDouble(slonA);
        double latB = Double.parseDouble(slatB);
        double lonB = Double.parseDouble(slonB);

        // calculation source: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude David George, Neeme Praks

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latB - latA);
        double lonDistance = Math.toRadians(lonB - lonA);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latA)) * Math.cos(Math.toRadians(latB))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2) + Math.pow(0, 2);
        double result = Math.sqrt(distance);

        return String.valueOf(result);
    }

}
