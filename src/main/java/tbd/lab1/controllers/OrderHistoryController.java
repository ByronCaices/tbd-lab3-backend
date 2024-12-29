package tbd.lab1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbd.lab1.collections.OrderHistoryCollection;
import tbd.lab1.services.OrderHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/order-history")
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("/")
    ResponseEntity<List<OrderHistoryCollection>> listOrderHistory() {
        try{
            List<OrderHistoryCollection> orderHistory = orderHistoryService.getAllOrderHistory();
            return ResponseEntity.ok(orderHistory);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
