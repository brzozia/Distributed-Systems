package pl.travel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping
public class ClientAPI {
    private final SourcesAPI sources;
    private List<Integer> airports;

    public ClientAPI() {
        this.sources = new SourcesAPI();
    }

    @GetMapping("/")
    public void GetStartPage(){
//        return sources.getAirports(input);
    }

    @GetMapping("/airports")
    public List<Integer> GetCityAirports(@RequestParam(name="input") String input){
        this.airports = sources.getCityAirports(input);
        return airports;
    }

    @GetMapping("/flights")
    public String GetFlights(
         @RequestParam(name="origin", required = true) String origin,
         @RequestParam(name="departure", required = false) String departure,
         @RequestParam(name="oneWay", required = false, defaultValue = false) boolean oneWay,
         @RequestParam(name="duration", required = false) int duration)
    {
        return sources.getAllFlightsResults(origin, departure, oneWay, duration);
    }


}
