package com.miaplicacion.CRUDMIXTO.repository;

import com.miaplicacion.CRUDMIXTO.entity.EmpleadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmpleadoProyectoRepository extends JpaRepository<EmpleadoProyecto, Long> {
    List<EmpleadoProyecto> findByEmpleadoId(Long empleadoId);
    List<EmpleadoProyecto> findByProyectoId(String proyectoId);
}
