package tbd.lab1.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import tbd.lab1.collections.UserNearestWarehousesCollection;

public interface UserNearestWarehousesRepository extends MongoRepository<UserNearestWarehousesCollection, String> {
    UserNearestWarehousesCollection findByUserId(String userId);
}
