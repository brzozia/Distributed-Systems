package pl.travel.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.travel.data.aeroDataAPI.Airports;
import pl.travel.data.amadeusAPI.FlightDestinations;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class AmadeusAPI {
    private String url;
    private RestTemplate restTemplate;
    private Map<?,?> apiKeys;

    public AmadeusAPI(RestTemplate restTemplate, Map<?,?> apiKeys) {
        this.url = "https://test.api.amadeus.com/v1";
        this.restTemplate = restTemplate;
        this.apiKeys = apiKeys;
    }

    private String getToken(){
        String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Request request = new Request();
        request.setClient_id((String) apiKeys.get("amadeus_client_id"));
        request.setClient_secret((String) apiKeys.get("amadeus_client_secret"));


        HttpEntity<Request> entity = new HttpEntity<>(request,requestHeaders);

        ResponseEntity<Response> result = restTemplate.exchange(
                tokenUrl, POST, entity, Response.class);

        if(result.getStatusCode()== HttpStatus.OK){
            System.out.println(result.getBody());
            return result.getBody().getAccess_token();
        }else {
            throw new IllegalArgumentException("No airports found.");
        }
    }

    public FlightDestinations getFlights(String origin, String departure, boolean oneWay, String duration, Integer maxPrice ) throws Exception {
        String url = this.url + "/shopping/flight-destinations?origin=" + origin + "&departureDate="
                +departure+"&oneWay="+oneWay+ (duration.equals("") ? ("&duration=" +duration) : "" )
                + "&nonStop=false" + (maxPrice>0 ? ("&maxPrice=" +maxPrice) : "" );

        System.out.println(url);
//        System.out.println(getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+apiKeys.get("amadeus"));

        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<FlightDestinations> flights = restTemplate.exchange(
                    url, GET, entity, FlightDestinations.class);
            System.out.println(flights.getBody());
            flights.getBody().getData().forEach(e -> System.out.println(e.getDestination()));

            return flights.getBody();
        }catch(Exception e){
            System.out.println(e);
            String[] tab = e.getMessage().split("\"");
            throw new Exception(tab[tab.length-6]+"\n"+tab[tab.length-2]);
        }
    }

    public Airports getAirports(String input) throws Exception {
        String url = this.url + "/reference-data/locations?subType=AIRPORT&keyword=" +input;

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+apiKeys.get("amadeus")); //TODO: delete

        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<Airports> airports = restTemplate.exchange(
                    url, GET, entity, Airports.class);
            System.out.println(airports.getBody());
            airports.getBody().getData().forEach(e -> System.out.println(e.getName()+" "+e.getAddress().getCityName()+" "+e.getAddress().getCountryName()));
            return airports.getBody();

        }catch(Exception e){
            System.out.println(e);
            String[] tab = e.getMessage().split("\"");
            throw new Exception(tab[tab.length-2]);
        }

    }

    private static class Request{
        private String grant_type;
        private String client_id;
        private String client_secret;

        public String getGrant_type() {
            return grant_type;
        }

        public void setGrant_type(String grant_type) {
            this.grant_type = grant_type;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }


        public Request() {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Response{
        private String access_token;


        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Response() {
        }
    }

}
