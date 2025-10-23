package com.miaplicacion.CRUDMIXTO.controller;

import com.miaplicacion.CRUDMIXTO.service.EmpleadoService;
import com.miaplicacion.CRUDMIXTO.service.ProyectoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EmpleadoService empleadoService;
    private final ProyectoService proyectoService;

    public HomeController(EmpleadoService empleadoService, ProyectoService proyectoService) {
        this.empleadoService = empleadoService;
        this.proyectoService = proyectoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var empleados = empleadoService.listarTodos();
        var proyectos = proyectoService.listarTodos();

        model.addAttribute("empleados", empleados);
        model.addAttribute("proyectos", proyectos);

        // ✅ Agregamos contadores
        model.addAttribute("totalEmpleados", empleados.size());
        model.addAttribute("totalProyectos", proyectos.size());

        return "home";
    }
}
