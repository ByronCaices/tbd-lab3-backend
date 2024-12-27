package tbd.lab1.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
