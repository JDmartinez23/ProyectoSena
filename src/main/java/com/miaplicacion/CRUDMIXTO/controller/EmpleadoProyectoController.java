package com.miaplicacion.CRUDMIXTO.controller;

import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import com.miaplicacion.CRUDMIXTO.entity.Proyecto;
import com.miaplicacion.CRUDMIXTO.entity.EmpleadoProyecto;
import com.miaplicacion.CRUDMIXTO.service.EmpleadoProyectoService;
import com.miaplicacion.CRUDMIXTO.service.EmpleadoService;
import com.miaplicacion.CRUDMIXTO.service.ProyectoService;
import com.miaplicacion.CRUDMIXTO.service.TareaService; // 👈 importar
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/asignaciones")
public class EmpleadoProyectoController {

    private final EmpleadoProyectoService asignacionService;
    private final EmpleadoService empleadoService;
    private final ProyectoService proyectoService;
    private final TareaService tareaService; // 👈 inyectar

    public EmpleadoProyectoController(EmpleadoProyectoService asignacionService,
                                      EmpleadoService empleadoService,
                                      ProyectoService proyectoService,
                                      TareaService tareaService) { // 👈 agregar en constructor
        this.asignacionService = asignacionService;
        this.empleadoService = empleadoService;
        this.proyectoService = proyectoService;
        this.tareaService = tareaService;
    }

    @GetMapping
    public String listar(Model model) {
        var empleados = empleadoService.listarTodos();
        var proyectos = proyectoService.listarTodos();

        Map<Long, String> empleadoMap = empleados.stream()
                .collect(Collectors.toMap(Empleado::getId, Empleado::getNombre));

        Map<String, String> proyectoMap = proyectos.stream()
                .collect(Collectors.toMap(Proyecto::getId, Proyecto::getNombre));

        var asignaciones = asignacionService.listarTodos();

        // Agregar tareas por cada asignación
        asignaciones.forEach(asig -> {
            asig.setTareas(tareaService.listarPorAsignacion(asig.getId()));
        });

        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("empleados", empleados);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("empleadoMap", empleadoMap);
        model.addAttribute("proyectoMap", proyectoMap);

        return "asignaciones/index";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("empleados", empleadoService.listarTodos());
        model.addAttribute("proyectos", proyectoService.listarTodos());
        return "asignaciones/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Long empleadoId, @RequestParam String proyectoId) {
        EmpleadoProyecto ep = new EmpleadoProyecto();
        ep.setEmpleadoId(empleadoId);
        ep.setProyectoId(proyectoId);
        asignacionService.guardar(ep);
        return "redirect:/asignaciones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return "redirect:/asignaciones";
    }
}
