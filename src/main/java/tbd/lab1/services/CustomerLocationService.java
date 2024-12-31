package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.collections.CustomerLocationCollection;
import tbd.lab1.repositories.CustomerLocationRepository;

@Service
public class CustomerLocationService {
    private final CustomerLocationRepository customerLocationRepository;

    @Autowired
    public CustomerLocationService(CustomerLocationRepository customerLocationRepository) {
        this.customerLocationRepository = customerLocationRepository;
    }

    public CustomerLocationCollection saveCustomerLocation(CustomerLocationCollection customerLocationCollection) {
        return customerLocationRepository.save(customerLocationCollection);
    }

    public CustomerLocationCollection getCustomerLocationByUserId(String userId) {
        return customerLocationRepository.findByUserId(userId);
    }
}
