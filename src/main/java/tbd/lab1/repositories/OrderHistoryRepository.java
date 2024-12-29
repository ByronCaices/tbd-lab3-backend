package tbd.lab1.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tbd.lab1.collections.OrderHistoryCollection;

import java.util.List;

public interface OrderHistoryRepository extends MongoRepository<OrderHistoryCollection, String> {
    OrderHistoryCollection findByIdUser(Integer idUser);
}
