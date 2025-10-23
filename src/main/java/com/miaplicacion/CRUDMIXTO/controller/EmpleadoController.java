package com.miaplicacion.CRUDMIXTO.controller;

import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import com.miaplicacion.CRUDMIXTO.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    // ✅ Listar con búsqueda
    @GetMapping
    public String lista(Model model, @RequestParam(value = "q", required = false) String q) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("empleados", service.buscarPorNombreOCargo(q));
        } else {
            model.addAttribute("empleados", service.listarTodos());
        }
        model.addAttribute("q", q);
        return "empleados/index";
    }

    // ✅ Nuevo empleado
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "empleados/nuevo";
    }

    // ✅ Guardar
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") Empleado empleado,
                          BindingResult br, Model model) {
        if (br.hasErrors()) {
            return "empleados/nuevo";
        }
        try {
            service.guardar(empleado);
        } catch (IllegalArgumentException ex) {
            br.rejectValue("email", "error.empleado", ex.getMessage());
            return "empleados/nuevo";
        }
        return "redirect:/empleados";
    }

    // ✅ Ver empleado
    @GetMapping("/ver/{id}")
    public String verEmpleado(@PathVariable Long id, Model model) {
        var op = service.buscarPorId(id);
        if (op.isEmpty()) return "redirect:/empleados";
        model.addAttribute("empleado", op.get());
        return "empleados/ver";
    }

    // ✅ Editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var op = service.buscarPorId(id);
        if (op.isEmpty()) return "redirect:/empleados";
        model.addAttribute("empleado", op.get());
        return "empleados/nuevo";
    }

    // ✅ Eliminar
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/empleados";
    }

    // ✅ Exportar Excel
    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportarExcel() {
        ByteArrayInputStream in = service.exportarExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=empleados.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    // ✅ Exportar PDF
    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportarPdf() {
        ByteArrayInputStream in = service.exportarPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=empleados.pdf");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}
