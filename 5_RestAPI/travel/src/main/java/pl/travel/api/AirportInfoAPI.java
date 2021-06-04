package pl.travel.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.travel.data.airportInfoAPI.AirportDetails;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

public class AirportInfoAPI {
    private final String url;
    private final RestTemplate restTemplate;
    private final Map<?,?> apiKeys;

    public AirportInfoAPI(RestTemplate restTemplate, Map<?,?> apiKeys) {
        this.restTemplate = restTemplate;
        this.url = "https://airport-info.p.rapidapi.com/airport";
        this.apiKeys = apiKeys;
    }

    public AirportDetails getDetails(String code){
        String url = this.url + "?iata="+ code;

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", (String) apiKeys.get("rapidapi_key"));
        headers.set("x-rapidapi-host", "airport-info.p.rapidapi.com");

        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<AirportDetails> airport = restTemplate.exchange(
                    url, GET, entity, AirportDetails.class);
            System.out.println(airport.getBody().getLocation());
            return airport.getBody();
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
