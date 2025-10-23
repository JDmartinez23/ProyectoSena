package com.miaplicacion.CRUDMIXTO.controller;

import com.miaplicacion.CRUDMIXTO.entity.Proyecto;
import com.miaplicacion.CRUDMIXTO.service.ProyectoService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService service;

    public ProyectoController(ProyectoService service) {
        this.service = service;
    }

    // ✅ Listar proyectos
    @GetMapping
    public String listar(Model model, @RequestParam(value = "q", required = false) String q) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("proyectos", service.buscarPorNombre(q));
        } else {
            model.addAttribute("proyectos", service.listarTodos());
        }
        model.addAttribute("q", q);
        return "proyectos/index";
    }

    // ✅ Cambiar estado de un proyecto
@PostMapping("/cambiarEstado/{id}")
public String cambiarEstado(@PathVariable String id) {
    var proyectoOpt = service.buscarPorId(id);
    if (proyectoOpt.isPresent()) {
        var proyecto = proyectoOpt.get();
        // Si está "Pendiente", lo pasamos a "Completado". Si no, a "Pendiente".
        if ("Pendiente".equalsIgnoreCase(proyecto.getEstado())) {
            proyecto.setEstado("Completado");
        } else {
            proyecto.setEstado("Pendiente");
        }
        service.guardar(proyecto);
    }
    return "redirect:/proyectos"; // 👈 vuelve al listado
}


    // ✅ Nuevo proyecto
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyectos/nuevo";
    }

    // ✅ Guardar proyecto
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("proyecto") Proyecto proyecto,
                          BindingResult br) {
        if (br.hasErrors()) {
            return "proyectos/nuevo";
        }
        service.guardar(proyecto);
        return "redirect:/proyectos";
    }

    // ✅ Ver proyecto
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable String id, Model model) {
        var op = service.buscarPorId(id);
        if (op.isEmpty()) return "redirect:/proyectos";
        model.addAttribute("proyecto", op.get());
        return "proyectos/ver";
    }

    // ✅ Editar proyecto
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        var op = service.buscarPorId(id);
        if (op.isEmpty()) return "redirect:/proyectos";
        model.addAttribute("proyecto", op.get());
        return "proyectos/nuevo";
    }

    // ✅ Eliminar proyecto
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        service.eliminar(id);
        return "redirect:/proyectos";
    }

    // ✅ Exportar a Excel
    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportarExcel() {
        ByteArrayInputStream in = service.exportarExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=proyectos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    // ✅ Exportar a PDF
    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportarPdf() {
        ByteArrayInputStream in = service.exportarPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=proyectos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
