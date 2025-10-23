package com.miaplicacion.CRUDMIXTO.repository;
import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    List<Empleado> findByNombreContainingIgnoreCaseOrCargoContainingIgnoreCase(String nombre, String cargo);
}

