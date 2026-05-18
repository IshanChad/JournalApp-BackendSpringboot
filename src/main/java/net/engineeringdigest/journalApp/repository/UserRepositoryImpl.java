package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate; // to be able to run this customized query

    public List<User> getUserForSA(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(
                Criteria.where("email").exists(true),
                Criteria.where("sentimentAnalysis").is(true)
        ));
        //query.addCriteria(Criteria.where("roles").in("USER","ADMIN"));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
