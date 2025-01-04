package tbd.lab1.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import tbd.lab1.entities.ProductoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;
}
