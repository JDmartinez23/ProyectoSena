package com.miaplicacion.CRUDMIXTO.repository;

import com.miaplicacion.CRUDMIXTO.entity.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);
}
