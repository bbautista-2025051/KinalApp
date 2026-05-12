package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarTodos();
    Producto guardar(Producto producto);
    Producto actualizar(Long id, Producto producto);
    Optional<Producto> buscarPorId(Long id);
    void eliminar(Long id);
    boolean existePorId(Long id);
    List<Producto> obtenerPorEstado(int estado);
}