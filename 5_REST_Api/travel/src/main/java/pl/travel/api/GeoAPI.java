package pl.travel.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.travel.data.Distance;

import static org.springframework.http.HttpMethod.GET;

public class GeoAPI {
    private RestTemplate restTemplate;
    private String url;

    public GeoAPI(RestTemplate restTemplate) {
        this.url = "https://geo-services-by-mvpc-com.p.rapidapi.com/distance";
        this.restTemplate = restTemplate;
    }

    public String getDistance(String latA, String lonA, String latB, String lonB){
        String url = this.url + "?locationB="+latB +","+ lonB + "&locationA="+latA + "," + lonA +"&unit=kms";

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", ""); //TODO: delete
        headers.set("x-rapidapi-host", "geo-services-by-mvpc-com.p.rapidapi.com");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Distance> distance = restTemplate.exchange(
                url, GET, entity, Distance.class);

        if(distance.getStatusCode()== HttpStatus.OK){
            System.out.println(distance.getBody().getData());

            return distance.getBody().getData();

        }else {
            throw new IllegalArgumentException("No airports found.");
        }
    }
}
