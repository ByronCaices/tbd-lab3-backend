package tbd.lab1.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "addresses")
public class AddressesCollection {
    @Id
    private String id;
    private String street;
    private String number;
    private String commune;
    private String city;
    private String region;
    private String country;
    private String postalCode;
    private String idUser;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime lastModifiedDate;
}
