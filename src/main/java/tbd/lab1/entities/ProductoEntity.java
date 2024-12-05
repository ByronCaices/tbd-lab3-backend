package tbd.lab1.entities;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class ProductoEntity {


    private Integer id_producto;

    private String nombre;

    private String descripcion;

    //Usamos BigDecimal porque proporciona precisión decimal exacta,
    // evita los errores de redondeo típicos de float y double, y
    // permite controlar precision y scale en cálculos financieros
    // y almacenamientos.
    private BigDecimal precio;

    private Integer stock;

    private String estado;

    private Integer id_categoria;
}

