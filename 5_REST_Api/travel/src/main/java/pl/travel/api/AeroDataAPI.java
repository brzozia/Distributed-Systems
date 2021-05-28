package pl.travel.api;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.travel.data.aeroDataAPI.Airports;

import static org.springframework.http.HttpMethod.GET;

public class AeroDataAPI {
    private String url;
    RestTemplate restTemplate;

    public AeroDataAPI(RestTemplate restTemplate) {
        this.url ="https://aerodatabox.p.rapidapi.com/airports/search/term";
        this.restTemplate = restTemplate;
    }

    public Airports getAirports(String input){
        String url = this.url + "?q=" + input;

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", ""); //TODO: delete
        headers.set("x-rapidapi-host", "aerodatabox.p.rapidapi.com");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Airports> airports = restTemplate.exchange(
                url, GET, entity, Airports.class);

        if(airports.getStatusCode()==HttpStatus.OK){
            return airports.getBody();
        }else {
            throw new IllegalArgumentException("No airports found.");
        }

    }
}
