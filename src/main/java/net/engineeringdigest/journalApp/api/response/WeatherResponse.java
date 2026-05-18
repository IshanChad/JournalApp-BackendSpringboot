package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {

    private Current current;

    @Getter
    @Setter
    public class Current{
        public int temperature;
        @JsonProperty("weather_description")
        public List<String> weatherDescriptions;
        public int feelslike;
    }


}
