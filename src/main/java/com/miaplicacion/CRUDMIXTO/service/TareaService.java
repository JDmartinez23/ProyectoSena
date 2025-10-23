package com.miaplicacion.CRUDMIXTO.service;

import com.miaplicacion.CRUDMIXTO.entity.Tarea;
import com.miaplicacion.CRUDMIXTO.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository repo;

    public TareaService(TareaRepository repo) {
        this.repo = repo;
    }

    public List<Tarea> listarTodos() {
        return repo.findAll();
    }

    public Optional<Tarea> buscarPorId(String id) {
        return repo.findById(id);
    }

    public List<Tarea> listarPorAsignacion(Long asignacionId) {
        return repo.findByAsignacionId(asignacionId);
    }

    public Tarea guardar(Tarea tarea) {
        return repo.save(tarea);
    }

    public void eliminar(String id) {
        repo.deleteById(id);
    }

    public void completar(String id) {
        var tarea = repo.findById(id);
        if (tarea.isPresent()) {
            tarea.get().setEstado("Completada");
            repo.save(tarea.get());
        }
    }
}
