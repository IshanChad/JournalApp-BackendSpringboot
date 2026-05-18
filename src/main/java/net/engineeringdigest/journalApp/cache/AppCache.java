package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//we wanted to place api in database, instead of hardcoded at service file, but loading it each time on api call from database
//will be causing latency, so let's keep it in cache from where this database request will happen only once at the start

@Component
public class AppCache {

    private final ConfigJournalAppRepository configJournalAppRepository;
    public enum keys {
        WEATHER_API
    }

    public Map<String, String> appCache = new HashMap<>();
    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    @PostConstruct
    public void init(){
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for (ConfigJournalAppEntity configJournalAppEntity : all){
            appCache.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }

}
