package tbd.lab1.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tbd.lab1.entities.ProductoEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "user_nearest_warehouses")
@NoArgsConstructor
@AllArgsConstructor
public class UserNearestWarehousesCollection {
    @Id
    private String id;
    private String userId;
    private List<Warehouse> warehouses;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Warehouse{
        private String warehouseId;
        private String warehouseName;
        private List<ProductoEntity> products;
        private double distance;
    }
}
