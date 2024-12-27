package tbd.lab1.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tbd.lab1.collections.AddressesCollection;

import java.util.List;

public interface AddressesRepository extends MongoRepository<AddressesCollection, String> {
    List<AddressesCollection> findByIdUser(String id_user);
}
