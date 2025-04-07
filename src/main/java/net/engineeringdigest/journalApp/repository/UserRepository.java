package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId>{
    User findByUserName(String username);

    void deleteByUserName(String username);
}

//journalApp/src/main/java/net/engineeringdigest/journalApp/repository/UserRepository.java