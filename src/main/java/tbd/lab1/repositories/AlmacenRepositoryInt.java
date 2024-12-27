package tbd.lab1.repositories;

import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.ProductoEntity;

import java.util.List;

public interface AlmacenRepositoryInt {
    AlmacenEntity saveAlmacen(AlmacenEntity almacen);
    List<AlmacenEntity> getAlmacenes();
    AlmacenEntity getAlmacenById(Integer id);
    boolean updateAlmacen(AlmacenEntity almacen);
    boolean deleteAlmacen(Integer id);
}