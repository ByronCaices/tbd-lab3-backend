package tbd.lab1.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "customer_locations")
public class CustomerLocationCollection {
    @Id
    private String id;
    private String userId;
    private double latitude;
    private double longitude;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;
}
