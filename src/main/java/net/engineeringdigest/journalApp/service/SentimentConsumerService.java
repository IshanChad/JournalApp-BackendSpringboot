package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//will consume from mails produced in UserScheduler
@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    //to make a kafka function we just need to add @KafkaListner there
    //it will check if any data came in kafka, if yes sendEmail function will be executed
    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){ sendEmail(sentimentData);}

    private void sendEmail(SentimentData sentimentData) {
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous work", sentimentData.getSentiment());
    }
}
