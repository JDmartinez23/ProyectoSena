package com.miaplicacion.CRUDMIXTO.migration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/migration")
public class DataMigrationController {

    private final CsvJsonImporter csvJsonImporter;
    private final SqlExcelImporter sqlExcelImporter;

    public DataMigrationController(CsvJsonImporter csvJsonImporter,
                                   SqlExcelImporter sqlExcelImporter) {
        this.csvJsonImporter = csvJsonImporter;
        this.sqlExcelImporter = sqlExcelImporter;
    }

    @PostMapping("/ejecutar")
    public String ejecutarMigracion(Model model) {
        try {
            csvJsonImporter.importarEmpleadosDesdeCsv("data/empleados.csv");
            csvJsonImporter.importarEmpleadosDesdeJson("data/empleados.json");
            sqlExcelImporter.importarProyectos();

            model.addAttribute("mensaje", "Migración completada correctamente.");
            return "migration_resultados";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error durante la migración: " + e.getMessage());
            return "migration_resultados";
        }
    }
}
