package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.collections.UserNearestWarehousesCollection;
import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.ProductoEntity;
import tbd.lab1.repositories.UserNearestWarehousesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@Service
public class NearestWarehousesService {
    private final UserNearestWarehousesRepository userNearestWarehousesRepository;
    private final AlmacenService almacenService;

    @Autowired
    public NearestWarehousesService(UserNearestWarehousesRepository userNearestWarehousesRepository, AlmacenService almacenService) {
        this.userNearestWarehousesRepository = userNearestWarehousesRepository;
        this.almacenService = almacenService;
    }

    public void calculateNearestWarehouses(String userId, double userLat, double userLon) {
        // Obtener datos de los almacenes desde PostGIS
        List<AlmacenEntity> warehouses = almacenService.getAllAlmacenes();

        List<UserNearestWarehousesCollection.Warehouse> nearestWarehouses = new ArrayList<>();

        if (warehouses.isEmpty()) {
            return;
        }

        if (userNearestWarehousesRepository.findByUserId(userId) != null) {
            userNearestWarehousesRepository.deleteByUserId(userId);
        }

        for (AlmacenEntity warehouse : warehouses) {
            String warehouseId = warehouse.getId_almacen().toString();
            String warehouseName = warehouse.getNombre();
            String lat = warehouse.getLatitud();
            String lon = warehouse.getLongitud();

            double distance = calculateDistance(userLat, userLon, Double.parseDouble(lat), Double.parseDouble(lon));

            UserNearestWarehousesCollection.Warehouse nearestWarehouse = new UserNearestWarehousesCollection.Warehouse();
            nearestWarehouse.setWarehouseId(warehouseId);
            nearestWarehouse.setWarehouseName(warehouseName);
            nearestWarehouse.setDistance(distance);

            nearestWarehouses.add(nearestWarehouse);
        }

        // Ordenar almacenes por distancia
        nearestWarehouses.sort(Comparator.comparingDouble(UserNearestWarehousesCollection.Warehouse::getDistance));

        // Guardar la lista ordenada en MongoDB
        UserNearestWarehousesCollection nearestWarehousesCollection = new UserNearestWarehousesCollection();
        nearestWarehousesCollection.setUserId(userId);
        nearestWarehousesCollection.setWarehouses(nearestWarehouses);

        userNearestWarehousesRepository.save(nearestWarehousesCollection);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distancia en km
    }

    public UserNearestWarehousesCollection getNearestWarehouses(String userId) {
        return userNearestWarehousesRepository.findByUserId(userId);
    }
}
