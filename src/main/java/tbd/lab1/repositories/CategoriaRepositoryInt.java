package tbd.lab1.repositories;
import tbd.lab1.entities.CategoriaEntity;

import java.util.List;

public interface CategoriaRepositoryInt {
    // Método para guardar una categoría
    CategoriaEntity saveCategoria(CategoriaEntity categoria);

    // Método para obtener todas las categorías
    List<CategoriaEntity> getCategorias();

    // Método para encontrar una categoría por su ID
    CategoriaEntity findByIdCategoria(Integer id);

    // Método para eliminar una categoría por su ID
    boolean deleteCategoria(Integer id);

    // Método para actualizar una categoría
    boolean updateCategoria(CategoriaEntity categoria);
}

