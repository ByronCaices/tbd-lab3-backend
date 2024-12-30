package tbd.lab1.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customer_locations")
public class CustomerLocationCollection {
    @Id
    private String id;
    private String userId;
    private double latitude;
    private double longitude;
}
