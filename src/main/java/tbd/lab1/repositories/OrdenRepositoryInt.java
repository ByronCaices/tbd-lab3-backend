package tbd.lab1.repositories;

import tbd.lab1.entities.OrdenEntity;

import java.util.List;

public interface OrdenRepositoryInt {
    OrdenEntity saveOrden(OrdenEntity orden);

    OrdenEntity getOrdenById(Integer id);
    List<OrdenEntity> getAllOrdenes();
    List<OrdenEntity> getOrdenes();

    boolean updateOrden(OrdenEntity orden);

    boolean deleteOrden(Integer id);
}
