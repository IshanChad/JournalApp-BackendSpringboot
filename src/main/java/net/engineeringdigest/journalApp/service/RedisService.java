package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

//to help us interact with redis
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    //we are making it generic so that it works for request with any kind of object passed
    public <T> T get(String key, Class<T> entityClass) {
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass); //we want object returned in entityClass form
        } catch (Exception e) {
            log.error("Exception ", e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try{
            redisTemplate.opsForValue().set(key, o, ttl);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
    }
}
