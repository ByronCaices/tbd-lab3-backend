package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.collections.AddressesCollection;
import tbd.lab1.repositories.AddressesRepository;

import java.util.List;

@Service
public class AddressesService {
    private final AddressesRepository addressesRepository;

    @Autowired
    public AddressesService(AddressesRepository addressesRepository) {
        this.addressesRepository = addressesRepository;
    }

    public AddressesCollection  saveAddresses(AddressesCollection address, String id_user) {
        address.setIdUser(id_user);
        System.out.println(address);
        return addressesRepository.save(address);
    }

    public List<AddressesCollection> getAddresses(String id_user) {
        return addressesRepository.findByIdUser(id_user);
    }

}
