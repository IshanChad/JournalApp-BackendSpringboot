package net.engineeringdigest.journalApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void interactingWithRedis() {
        redisTemplate.opsForValue().set("email","email@gmail.com");
        Object salary = redisTemplate.opsForValue().get("salary");
    }
    // to have redis in springboot and redis in command line syncrhonised, we need to set same serializor and deserializor as done in /config/RedisConfig, though could be done in JournalApplication like RestTemplate
}
