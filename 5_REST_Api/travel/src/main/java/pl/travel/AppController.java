package pl.travel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.travel.data.aeroDataAPI.Airports;
import pl.travel.data.amadeusAPI.FlightDestinations;

import java.io.IOException;

//@RestController
@Controller
public class AppController {
    private final SourcesAPI sources;

    public AppController() throws IOException {
        this.sources = new SourcesAPI();
    }

    @GetMapping("/")
    public String GetStartPage(Model model){
        model.addAttribute("input", "");

        return "index";
    }

    @GetMapping("/airports")
    public String GetCityAirports(@RequestParam(name="input") String input, Model model){
        Airports airports;
        try{
            airports = sources.getCityAirports(input);
            airports.getData().forEach(air -> {
                String newName = air.getName()+" | "+ air.getAddress().getCityName()+" "+air.getAddress().getCountryName();
                air.setName(newName);}
            );
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        model.addAttribute("origin", "");
        model.addAttribute("departure", "2021-05-29");
        model.addAttribute("oneWay", false);
        model.addAttribute("duration", "");
        model.addAttribute("maxPrice", "");

        System.out.println("halo "+input);
        model.addAttribute("airports", airports.getData());
        return "form";
    }

    @GetMapping("/flights")
    public String GetFlights(
         @RequestParam(name="origin", required = true) String origin,
         @RequestParam(name="departure", required = false, defaultValue = "2021-05-29") String departure,
         @RequestParam(name="oneWay", required = false, defaultValue = "false") Boolean oneWay,
         @RequestParam(name="duration", required = false, defaultValue = "") String duration,
         @RequestParam(name="maxPrice", required = false, defaultValue = "0") Integer maxPrice,
        Model model
    ){
        System.out.println(origin+" "+departure+" "+oneWay+" "+duration);
        model.addAttribute("origin", origin);

        try {
            FlightDestinations flights = sources.getAllFlightsResults(origin, departure, oneWay, duration, maxPrice);
            model.addAttribute("flights", flights.getData());
            return "flights";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


}
