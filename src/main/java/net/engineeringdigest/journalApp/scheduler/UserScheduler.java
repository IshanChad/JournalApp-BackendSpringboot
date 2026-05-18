package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserRepositoryImpl userRepository;
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    @Autowired //kind of <key,value> that will be sent
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

   // @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 0 * * 0")
    public void fetchUsersAndSendMail(){
        List<User> users = userRepository.getUserForSA();
        for(User user : users) {
            List<Sentiment> sentiments = user.getJournalEntries().stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                //emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());

                //here we did the job for kafka producer i.e. sending email for SentimentConsumerService
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment.toString()).build();
                kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData); //sending email as key and sentimentData as data on topic weekly-sentiments. key sent so that now for data will go in same partition due to same key

                // kafka fallback - if kafka doesn't work send mail manually
//                try{
//                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
//                } catch (Exception e) {
//                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous work", sentimentData.getSentiment());
//                }
            }
        }
    }
    // that on every sunday, search for users who opted for SA and mail it to them (by finding which sentiment was most frequent in last 7 days)

    //every 5 minutes reload appCache to ensure if api is updated, application reloads to contain it
    @Scheduled(cron = "0 */5 * * * *")
    public void clearAppCache() {
        appCache.init();
    }
}
