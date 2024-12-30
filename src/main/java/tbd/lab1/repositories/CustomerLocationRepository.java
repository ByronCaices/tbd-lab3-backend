package tbd.lab1.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tbd.lab1.collections.CustomerLocationCollection;

public interface CustomerLocationRepository extends MongoRepository<CustomerLocationCollection, String> {
    CustomerLocationCollection findByUserId(String userId);
}
