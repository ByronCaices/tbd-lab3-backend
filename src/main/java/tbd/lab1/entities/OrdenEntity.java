package tbd.lab1.entities;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class OrdenEntity {

    private Integer id_orden;

    private LocalDateTime fecha_orden;

    private String estado;

    private Integer id_cliente;

    private Integer id_almacen;

    private BigDecimal total;
}
