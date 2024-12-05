package tbd.lab1.entities;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleOrdenEntity {

    private Integer id_detalle;

    private Integer id_orden;

    private Integer id_producto;

    private Integer cantidad;

    private BigDecimal precio_unitario;
}
