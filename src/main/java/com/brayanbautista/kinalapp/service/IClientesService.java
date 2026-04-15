package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClientesService {
    //Interfaz es un contrato que dice QUE METODOS debe tener
    //cualquier servicio de Clientes,  No tiene implemetnacion
    //solo la definicion de los metodos

    //Metodo que devuelve una lista de todos los Clientes
    List<Cliente> listarTodos();
    //Liste<Cliente> lo que hace es devolver una lista
    //de objeto de la entidad Clientes

    //Metodo que guarda un Cliente en la BD
    Cliente guardar(Cliente cliente);
    //Parametros  -Recibe un objeto Cliente con los datos a guardar


    Cliente actualizar(String dpi, Cliente cliente);

    //Optional - Contenedor que puede o no tener un valor
    //evitar el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);
    //Parametros - dpi: DPI del Cliente a actualizar
    //Cliente cliente: Objeto con los datos nuevos
    //Retorna un objeto de tipo Cliente ya actualizado

    //Metodo de tipo void para eliminar a un Cliente
    //void: no retorna ningun dato
    //Elimina un Cliente por su DPI
    void eliminar(String dpi);

    //boolean - retorna un valor que es tru si existe o false si no existe
    boolean existePorDPI(String dpi);

    List<Cliente> obtenerPorEstado(int estado);

    //
}
