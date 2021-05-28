package pl.travel.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.travel.data.BingImages;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

public class BingImagesAPI {
    private final String url;
    private final RestTemplate restTemplate;
    private final Map<?,?> apiKeys;

    public BingImagesAPI(RestTemplate restTemplate, Map<?,?> apiKeys) {
        this.restTemplate = restTemplate;
        this.url = "https://bing-image-search1.p.rapidapi.com/images/search";
        this.apiKeys = apiKeys;
    }

    public String getPhoto(String input){
        String url = this.url + "?q="+ input.replace(" ","");

        System.out.println(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", (String) apiKeys.get("rapidapi_key"));
        headers.set("x-rapidapi-host", "bing-image-search1.p.rapidapi.com");

        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<BingImages> img = restTemplate.exchange(
                    url, GET, entity, BingImages.class);
            System.out.println(img.getBody().getValue().get(0));

            return img.getBody().getValue().get(0).getThumbnailUrl();
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
