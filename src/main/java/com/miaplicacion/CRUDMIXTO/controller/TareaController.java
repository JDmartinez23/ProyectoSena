package com.miaplicacion.CRUDMIXTO.controller;

import com.miaplicacion.CRUDMIXTO.entity.Tarea;
import com.miaplicacion.CRUDMIXTO.service.EmpleadoProyectoService;
import com.miaplicacion.CRUDMIXTO.service.TareaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final EmpleadoProyectoService asignacionService;

    public TareaController(TareaService tareaService, EmpleadoProyectoService asignacionService) {
        this.tareaService = tareaService;
        this.asignacionService = asignacionService;
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarTodos());
        return "tareas/nueva";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Long asignacionId,
                          @RequestParam String descripcion,
                          @RequestParam String estado) {
        Tarea tarea = new Tarea();
        tarea.setAsignacionId(asignacionId);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);

        tareaService.guardar(tarea);
        return "redirect:/asignaciones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        tareaService.eliminar(id);
        return "redirect:/asignaciones";
    }

    @PostMapping("/completar/{id}")
    public String completar(@PathVariable String id) {
        tareaService.completar(id);
        return "redirect:/asignaciones";
    }
}
