package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    void eliminar(Long id);
    boolean existePorId(Long id);
    List<Usuario> obtenerPorEstado(int estado);
    boolean existePorUsername(String username);
}