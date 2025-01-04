package tbd.lab1.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbd.lab1.collections.AddressesCollection;
import tbd.lab1.services.AddressesService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressesController {
    private final AddressesService addressesService;

    @Autowired
    public AddressesController(AddressesService addressesService) {
        this.addressesService = addressesService;
    }

    @PostMapping("/save-addresses/{id_user}")
    ResponseEntity<AddressesCollection> saveAddresses(@RequestBody AddressesCollection addresses, @PathVariable String id_user) {
        try {
            AddressesCollection response = addressesService.saveAddresses(addresses, id_user);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-addresses/{id_user}")
    ResponseEntity<List<AddressesCollection>> getAddresses(@PathVariable String id_user) {
        try {
            List<AddressesCollection> response = addressesService.getAddresses(id_user);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete-addresses/{id_address}")
    ResponseEntity<Boolean> deleteAddresses(@PathVariable String id_address) {
        try {
            addressesService.deleteAddresses(id_address);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-addresses")
    ResponseEntity<AddressesCollection> updateAddresses(@RequestBody AddressesCollection addresses) {
        try {
            AddressesCollection response = addressesService.updateAddresses(addresses);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
