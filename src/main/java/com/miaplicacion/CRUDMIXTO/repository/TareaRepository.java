package com.miaplicacion.CRUDMIXTO.repository;

import com.miaplicacion.CRUDMIXTO.entity.Tarea;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TareaRepository extends MongoRepository<Tarea, String> {
    List<Tarea> findByAsignacionId(Long asignacionId);
}
