package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.collections.OrderHistoryCollection;
import tbd.lab1.entities.DetalleOrdenEntity;
import tbd.lab1.entities.OrdenEntity;
import tbd.lab1.entities.ProductoEntity;
import tbd.lab1.repositories.OrderHistoryRepository;

import java.util.ArrayList;

@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    // Services
    private final OrdenService ordenService;

    private final ProductoService productoService;

    private final DetalleOrdenService detalleOrdenService;

    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository, OrdenService ordenService, ProductoService productoService, DetalleOrdenService detalleOrdenService) {
        this.orderHistoryRepository = orderHistoryRepository;
        this.ordenService = ordenService;
        this.productoService = productoService;
        this.detalleOrdenService = detalleOrdenService;
    }

    public void syncOrderHistory() {
        // Leer órdenes y productos de PostgreSQL
        ArrayList<OrdenEntity> orders = ordenService.getAllOrdenes();
        ArrayList<ProductoEntity> products = productoService.getAllProductos();
        ArrayList<DetalleOrdenEntity> orderDetails = detalleOrdenService.getDetalle();

        orderDetails.forEach(orderDetail -> {
            // Primero se obtiene la orden que corresponde al detalle
            OrdenEntity order = orders.stream().filter(o -> o.getId_orden().equals(orderDetail.getId_orden())).findFirst().orElse(null);
            if (order == null) {
                return;
            }

            // Se utiliza el id_producto del detalle para obtener el producto correspondiente
            OrderHistoryCollection orderHistory = orderHistoryRepository.findByIdUser(order.getId_cliente());
            if (orderHistory == null) {
                orderHistory = new OrderHistoryCollection();
                orderHistory.setIdUser(order.getId_cliente());
                orderHistory.setOrders(new ArrayList<>());
            }

            // Si la lista de ordenes no está vacía, se verifica si la orden ya existe
            if (orderHistory.getOrders().stream().anyMatch(o -> o.getIdOrder().equals(order.getId_orden()))) {
                return;
            }

            OrderHistoryCollection.Order newOrder = new OrderHistoryCollection.Order();
            newOrder.setIdOrder(order.getId_orden());
            newOrder.setDate(order.getFecha_orden().toString());
            newOrder.setTotal(order.getTotal());
            newOrder.setStatus(order.getEstado());

            // Se obtiene el producto correspondiente al detalle
            ProductoEntity product = products.stream().filter(p -> p.getId_producto().equals(orderDetail.getId_producto())).findFirst().orElse(null);
            if (product == null) {
                return;
            }
            // Se crea un nuevo producto para la orden
            OrderHistoryCollection.Product newProduct = new OrderHistoryCollection.Product();
            newProduct.setIdProduct(product.getId_producto());
            newProduct.setName(product.getNombre());
            newProduct.setPrice(product.getPrecio().toString());
            newProduct.setQuantity(orderDetail.getCantidad());

            // Luego se agrega el producto a la orden
            newOrder.setProduct(newProduct);

            // Se inserta la orden en la lista de órdenes
            orderHistory.getOrders().add(newOrder);

            // Se guarda la lista de órdenes en la base de datos
            orderHistoryRepository.save(orderHistory);
        });
    }
}

