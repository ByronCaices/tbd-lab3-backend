package tbd.lab1.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import tbd.lab1.models.ClienteMultiplesCompras;
import tbd.lab1.models.UsuarioActivo;

import java.util.List;

@Repository
public class AuditRepository implements AuditRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    public List<UsuarioActivo> reporteUsuariosMasActivos() {
        String sql = "SELECT * FROM reporte_usuarios_mas_activos()";

        try (Connection connection = sql2o.open()) {
            return connection.createQuery(sql)
                    .executeAndFetch(UsuarioActivo.class);
        }
    }

    public ClienteMultiplesCompras clientesMultiplesCompras() {
        String sql = """
                    WITH compras_por_dia AS (
                        SELECT
                            o.id_cliente,
                            DATE(o.fecha_orden) AS fecha,
                            COUNT(o.id_orden) AS num_compras
                        FROM
                            orden o
                        WHERE
                            o.fecha_orden >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month' AND
                            o.fecha_orden < DATE_TRUNC('month', CURRENT_DATE)
                        GROUP BY
                            o.id_cliente, DATE(o.fecha_orden)
                        HAVING
                            COUNT(o.id_orden) > 1
                    ),
                    productos_comprados AS (
                        SELECT
                            cpd.id_cliente,
                            cpd.fecha,
                            p.nombre AS producto
                        FROM
                            compras_por_dia cpd
                        JOIN orden o ON cpd.id_cliente = o.id_cliente AND DATE(o.fecha_orden) = cpd.fecha
                        JOIN detalle_orden d ON o.id_orden = d.id_orden
                        JOIN producto p ON d.id_producto = p.id_producto
                    )
                    SELECT
                        COUNT(DISTINCT id_cliente) AS num_clientes,
                        ARRAY_TO_STRING(ARRAY_AGG(DISTINCT producto), ', ') AS productos_comprados
                    FROM
                        productos_comprados;
                """;

        try (Connection connection = sql2o.open()) {
            return connection.createQuery(sql)
                    .executeAndFetchFirst(ClienteMultiplesCompras.class);
        }
    }
}
