package tbd.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbd.lab1.collections.CustomerLocationCollection;
import tbd.lab1.services.CustomerLocationService;
import tbd.lab1.services.NearestWarehousesService;

@RestController
@RequestMapping("/api/locations")
public class NearestWarehousesController {
    private final CustomerLocationService customerLocationService;
    private final NearestWarehousesService nearestWarehousesService;

    @Autowired
    public NearestWarehousesController(CustomerLocationService customerLocationService, NearestWarehousesService nearestWarehousesService) {
        this.customerLocationService = customerLocationService;
        this.nearestWarehousesService = nearestWarehousesService;
    }

    @PostMapping("/")
    public ResponseEntity<CustomerLocationCollection> saveLocation(@RequestBody CustomerLocationCollection location){
        try {
            CustomerLocationCollection savedLocation = customerLocationService.saveCustomerLocation(location);
            nearestWarehousesService.calculateNearestWarehouses(savedLocation.getUserId(), savedLocation.getLatitude(), savedLocation.getLongitude());
            return ResponseEntity.ok(savedLocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
