package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    //this is a passkey to avail a service/make API call from that server/website providing it
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;
    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null) { //if already present in redis, return it
            return weatherResponse;
        }
        else {
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace("<city>", city).replace("<apiKey>", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body!=null) { //if was not in redis, put it now
                redisService.set("weather_of_" + city, body, 300l);
            }
            return body;
        }
    }

    //null is to handle headers and all
    //and then deserializing/converting json into java object(POJO) like in Weather Response

    //example of post request
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key","value");
//        String requestBody = "{\n"+
//                "  \"username\":\"vipul\",\n" +
//                "  \"password\":\"vipul\",\n" +
//                "}  ";
//        User user = User.builder().username("vipul").password("vipul").build();
//        HttpEntity<String> httpEntity = new HttpEntity<>(user);
//        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherResponse.class);

}
