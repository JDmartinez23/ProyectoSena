package com.miaplicacion.CRUDMIXTO.service;

import com.miaplicacion.CRUDMIXTO.entity.EmpleadoProyecto;
import com.miaplicacion.CRUDMIXTO.repository.EmpleadoProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmpleadoProyectoService {
    private final EmpleadoProyectoRepository repo;

    public EmpleadoProyectoService(EmpleadoProyectoRepository repo) {
        this.repo = repo;
    }

    public List<EmpleadoProyecto> listarTodos() {
        return repo.findAll();
    }

    public EmpleadoProyecto guardar(EmpleadoProyecto ep) {
        return repo.save(ep);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<EmpleadoProyecto> buscarPorEmpleado(Long empleadoId) {
        return repo.findByEmpleadoId(empleadoId);
    }

    public List<EmpleadoProyecto> buscarPorProyecto(String proyectoId) {
        return repo.findByProyectoId(proyectoId);
    }
}
