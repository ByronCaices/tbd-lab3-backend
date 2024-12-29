package tbd.lab1.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tbd.lab1.entities.OrdenEntity;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "order_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryCollection {
    @Id
    private String id;
    private Integer idUser;
    private List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        private Integer idOrder;
        private String date;
        private Product product;
        private BigDecimal total;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private Integer idProduct;
        private String name;
        private Integer quantity;
        private String price;
    }
}
